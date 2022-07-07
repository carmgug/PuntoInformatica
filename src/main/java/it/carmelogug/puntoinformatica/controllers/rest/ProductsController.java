package it.carmelogug.puntoinformatica.controllers.rest;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.Store;
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

    @PreAuthorize("hasAuthority('puntoinformatica-admin')")
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Product product){

        try{
            Product p=productService.addProduct(product);
            return new ResponseEntity(new ResponseMessage("Added successful!",p),HttpStatus.OK);
        } catch (ProductAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product already exist!",e);
        }

    }//create

    @PreAuthorize("hasAuthority('puntoinformatica-admin')")
    @DeleteMapping("/{product}")
    public ResponseEntity banProduct(@PathVariable(value = "product") Product product){

        try {
            Product bannedProduct=productService.banProduct(product);
            return new ResponseEntity<>(new ResponseMessage("Product has been banned!",bannedProduct),HttpStatus.OK);

        }catch (ProductNotExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product not exist!", e);
        }
    }//banProduct

    @PreAuthorize("hasAuthority('puntoinformatica-admin')")
    @PutMapping("/{product}")
    public ResponseEntity unBanProduct(@PathVariable(value = "product") Product product){

        try {
            Product bannedProduct=productService.unBanProduct(product);
            return new ResponseEntity<>(new ResponseMessage("Product has been unBanned!",bannedProduct),HttpStatus.OK);

        }catch (ProductNotExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product not exist!", e);
        }
    }//unBanProduct









    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        List<Product> result= productService.showAllProducts();
        if(result.size()==0){
            return new ResponseEntity<>(new ResponseMessage("No result!"),HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }//getAll

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
            return new ResponseEntity<>(new ResponseMessage("No result!", result),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("Results found",result),HttpStatus.OK);
    }//getByNameAndTypeAndCategory

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
    }//getById

}
