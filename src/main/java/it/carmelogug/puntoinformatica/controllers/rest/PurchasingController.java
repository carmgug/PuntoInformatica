package it.carmelogug.puntoinformatica.controllers.rest;

import it.carmelogug.puntoinformatica.entities.Purchase;
import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.services.PurchasingService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.exceptions.Purchase.DateWrongRangeException;
import it.carmelogug.puntoinformatica.support.exceptions.Purchase.QuantityProductUnvailableException;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.User.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/{user}")
    public ResponseEntity getPurchasesInPeriod(
            @PathVariable (value = "user") User user,
            @RequestParam (value = "startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam (value = "endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        System.out.println("id");
        try {
            List<Purchase>result = purchasingService.getPurchasesByUserInPeriod(user, startDate, endDate);
            if (result.size() <= 0) {
                return new ResponseEntity(new ResponseMessage("No result!"), HttpStatus.OK);
            }
            return new ResponseEntity(result,HttpStatus.OK);

        }catch(UserNotFoundException | DateWrongRangeException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }



}
