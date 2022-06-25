package it.carmelogug.puntoinformatica.controllers.rest;


import it.carmelogug.puntoinformatica.entities.Store;
import it.carmelogug.puntoinformatica.entities.StoredProduct;
import it.carmelogug.puntoinformatica.services.StoreService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Store store){
        try{
            storeService.addStore(store);
        }catch (StoreAlreadyExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Store already exist!",e);
        }
        return new ResponseEntity(new ResponseMessage("Store added successful!"),HttpStatus.OK);
    }

    /*
        Handler per gestire i casi in cui è stato passato un oggetto non conforme ai vincoli esplicitati.
        Restituisce i campi della classe e il messaggio di errore associato.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    //TODO Gestire con entity manager la consistenza.
    //TODO Gestire la rimozione dello store e dei relativi prodotti.
    @DeleteMapping
    public ResponseEntity delete(@RequestBody @Valid Store store){
        try{
            storeService.removeStore(store);
        }catch (StoreNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Store not exist!",e);
        }
        return new ResponseEntity<>(new ResponseMessage("Product has been deleted"),HttpStatus.OK);

    }

    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        List<Store> result= storeService.showAllStores();
        if(result.size()==0){
            return new ResponseEntity<>(new ResponseMessage("No result!"),HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @GetMapping("/search/by_varparams")
    public ResponseEntity getByCountryAndRegionAndCityAndAddress(
                @RequestParam(required = false) String country,
                @RequestParam(required = false) String region,
                @RequestParam(required = false) String city,
                @RequestParam(required = false) String province,
                @RequestParam(required = false) String address) {

        List<Store> result= storeService.showStoresByCountryAndRegionAndCityAndProvinceAndAddress(
                country,region,city,province,address);
        if(result.size()==0){
            return new ResponseEntity<>(new ResponseMessage("No result!"),HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    //TODO restituire i prodotti venduti in quel negozio.

    @GetMapping("/getProducts")
    public ResponseEntity getProducts(@RequestParam int storeID){
        try{

            Store store=storeService.showStoreByStoreID(storeID);
            List<StoredProduct> storedProducts= store.getStoredProducts();
            if(storedProducts.size()<=0) return new ResponseEntity<>(new ResponseMessage("No result!"),HttpStatus.OK);
            return new ResponseEntity<>(storedProducts,HttpStatus.OK);

        }catch (StoreNotExistException e){
            return new ResponseEntity<>(new ResponseMessage("Store not exist!"),HttpStatus.BAD_REQUEST);
        }

    }









}
