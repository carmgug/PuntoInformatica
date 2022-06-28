package it.carmelogug.puntoinformatica.controllers.rest;

import it.carmelogug.puntoinformatica.entities.Purchase;
import it.carmelogug.puntoinformatica.services.PurchasingService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.exceptions.Purchase.QuantityProductUnvailableException;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/purchase")
public class PurchasingController {

    @Autowired
    private PurchasingService purchasingService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Purchase purchase){
        try{
            return new ResponseEntity(new ResponseMessage(
                    "Purchase order has been added!",purchasingService.addPurchase(purchase))
                    ,HttpStatus.OK);
        }catch (QuantityProductUnvailableException | StoredProductNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }

}
