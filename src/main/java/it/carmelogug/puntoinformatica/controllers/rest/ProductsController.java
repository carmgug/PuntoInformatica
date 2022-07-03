package it.carmelogug.puntoinformatica.controllers.rest;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.services.ProductService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.Utilities;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;


    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Product product){
        Product p;
        try{
            p=productService.addProduct(product);
        } catch (ProductAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product already exist!",e);
        }
        return new ResponseEntity(new ResponseMessage("Added successful!",p),HttpStatus.OK);
    }


    //TODO Gestire le transazioni nel database tramite l'entity manager nel caso delle eliminazioni.

    @DeleteMapping
    public ResponseEntity delete(@RequestParam(required = true) long barCode,
                                    @RequestParam(required = true) Product.Type type,
                                    @RequestParam(required = true) Product.Category category){
        Product p;
        try {
            p=productService.removeProduct(barCode, type, category);
        }catch (ProductNotExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product not exist!", e);
        }
        return new ResponseEntity<>(new ResponseMessage("Product has been deleted",p),HttpStatus.OK);
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

    /*
        Handler per gestire casi in cui viene passato un tipo o una categoria non esistente per il prodotto creato.
        Restituisce il tipo aspettato, il valore trasmesso, e i valori possibili.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public String handleInvalidFormatExceptions(InvalidFormatException ex) {
        StringBuilder sb=new StringBuilder();
        sb.append("Mapping failure on field:"+ ex.getPathReference()+"\n");
        sb.append("Expected type: " + ex.getTargetType().getSimpleName()+"\n");
        sb.append("Provided value: " + ex.getValue()+"\n");
        sb.append("Expected values: [");
        for(Object f:ex.getTargetType().getEnumConstants()){
            sb.append(f.toString()+" ");
        }
        sb.append("]\n");
        return sb.toString();
    }



    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        List<Product> result= productService.showAllProducts();
        if(result.size()==0){
            return new ResponseEntity<>(new ResponseMessage("No result!"),HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    /*
        name: può essere null, se vuoto viene contato come null, se ha solo spazi non restituirà i nomi dei prodotti contenenti spazi
        type: può essere null, viene gestita l'eccezzione nel caso in cui venga passato un type non esistente
        category: può essere null, viene gestita l'eccezzione nel caso in cui venga passato un category non esistente.
     */
    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @GetMapping("/search/by_name_type_category")
    public ResponseEntity getByNameAndTypeAndCategory(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) Product.Type type,
                                    @RequestParam(required = false) Product.Category category){

        name= Utilities.upperCase(name,true);
        List<Product> result= productService.showProductsByNameAndTypeAndCategory(name,type,category);
        if(result.size()<=0){
            return new ResponseEntity<>(new ResponseMessage("No result!", null),HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @GetMapping("/search/by_id")
    public ResponseEntity getById(@RequestParam(required = true) String id){
        System.out.println(id);
        try {
            Product result = productService.searchProductById(Integer.parseInt(id));
            return new ResponseEntity<>(new ResponseMessage("Product found",result),HttpStatus.OK);
        }catch (ProductNotExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not exist!", e);
        }
    }

    /*
        Handler per gestire casi in cui viene passato un tipo o una categoria non esistente per la ricerca/eliminazione di un prodotto
        Restituisce il tipo aspettato, il valore trasmesso, e i valori possibili.
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConversionFailedException.class)
    public String handleConversionFailedException(ConversionFailedException ex) {
        StringBuilder sb=new StringBuilder();
        sb.append("Expected type: " + ex.getTargetType().getType().getSimpleName()+"\n");
        sb.append("Provided value: " + ex.getValue()+"\n");
        sb.append("Expected values: [");
        for(Object f:ex.getTargetType().getType().getEnumConstants()){
            sb.append(f.toString()+" ");
        }
        sb.append("]\n");
        String msg=sb.toString();

        return msg;

    }




}
