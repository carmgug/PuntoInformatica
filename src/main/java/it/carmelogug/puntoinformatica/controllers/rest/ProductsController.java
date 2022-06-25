package it.carmelogug.puntoinformatica.controllers.rest;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import it.carmelogug.puntoinformatica.entities.Product;
import it.carmelogug.puntoinformatica.services.ProductService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.exceptions.ProductAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.reflect.Field;
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
        try{
            productService.addProduct(product);
        } catch (ProductAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product already exist!",e);
        }
        return new ResponseEntity(new ResponseMessage("Added successful!"),HttpStatus.OK);
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




}
