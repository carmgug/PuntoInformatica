

package it.carmelogug.puntoinformatica.controllers.rest;

/*
import it.carmelogug.puntoinformatica.entities.StoredProduct;
import it.carmelogug.puntoinformatica.services.StoredProductService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductAlreadyExistException;
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
@RequestMapping("/storedProducts")
public class StoredProductController {

    @Autowired
    private StoredProductService storedProductService;


    @PostMapping
    public ResponseEntity create(@RequestBody @Valid StoredProduct storedProduct){
        try{
            storedProductService.addStoredProduct(storedProduct);
        }catch (StoredProductAlreadyExistException | ProductNotExistException | StoreNotExistException e){

            if( e instanceof StoredProductAlreadyExistException)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This product has already been stored in that store",e);
            else if( e instanceof ProductNotExistException)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This product not exist!",e);
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This store not exist!",e);
        }
        return new ResponseEntity(new ResponseMessage("Product has been stored!"),HttpStatus.OK);
    }

    /*
        Handler per gestire i casi in cui è stato passato un oggetto non conforme ai vincoli esplicitati.
        Restituisce i campi della classe e il messaggio di errore associato.
     */
/*
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

    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        List<StoredProduct> result= storedProductService.showAllStoredProducts();
        if(result.size()==0){
            return new ResponseEntity<>(new ResponseMessage("No result!"),HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }



}

*/
