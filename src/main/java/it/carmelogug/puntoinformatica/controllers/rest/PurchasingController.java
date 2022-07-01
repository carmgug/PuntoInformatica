package it.carmelogug.puntoinformatica.controllers.rest;

import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import it.carmelogug.puntoinformatica.entities.purchasing.Purchase;
import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import it.carmelogug.puntoinformatica.services.PurchasingService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.exceptions.Purchasing.*;
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
@RequestMapping("/purchasing")
public class PurchasingController {

    @Autowired
    private PurchasingService purchasingService;


    /*
        Metodi per la gestione degli acquisti.
     */

    /* Metodo da eliminare
    @PostMapping("/purchase")
    public ResponseEntity createPurchase(@RequestBody @Valid Purchase purchase){
        try{
            Purchase result=purchasingService.addPurchase(purchase);
            return new ResponseEntity(new ResponseMessage(
                    "Purchase order has been added!",result)
                    ,HttpStatus.OK);
        }catch (QuantityProductUnvailableException | StoredProductNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }
    */

    @PostMapping("/purchase/{cart}")
    public ResponseEntity createPurchase(@PathVariable(value = "cart") Cart cart) {
        try{
            Purchase result=purchasingService.addPurchase(cart);
            return new ResponseEntity(new ResponseMessage(
                    "Purchase order has been added!",result)
                    ,HttpStatus.OK);
        }catch (QuantityProductUnvailableException | CartIsEmptyException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }


    @GetMapping("/purchases/{user}")
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

    /*
        Metodi per la gestione del carrello
     */



    @PostMapping("/cart/{user}")
    public ResponseEntity createCart(@PathVariable (value = "user") User user){
        try {
            Cart cart = purchasingService.addCart(user);
            return new ResponseEntity(new ResponseMessage("Cart created successfully!",cart),HttpStatus.OK);
        }catch (UserNotFoundException | CartAlreadyExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }

    @PutMapping("/cart/{user}")
    public ResponseEntity addStoredProductToCart(@PathVariable (value = "user") User user,
                                                 @RequestBody StoredProduct storedProduct,
                                                 @RequestParam (value = "quantity",defaultValue = "1") int quantity){
        try{
            Cart cart = purchasingService.addStoredProductToCart(user,storedProduct,quantity);
            return new ResponseEntity(new ResponseMessage("StoredProduct added successfully!",cart),HttpStatus.OK);
        }catch (UserNotFoundException | CartNotExistException | StoredProductNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }


    }

    @DeleteMapping("cart/{user}/{storedProduct}")
    public ResponseEntity removeStoredProductFromCart(@PathVariable (value ="user") User user,
                                                      @PathVariable (value = "storedProduct") StoredProduct storedProduct){

        try{
            Cart cart= purchasingService.removeStoredProductFromCart(user,storedProduct);
            return new ResponseEntity(new ResponseMessage("Product has been deleted from the cart!",cart),HttpStatus.OK);
        } catch (StoredProductNotInCart| CartNotExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }

    }


    @GetMapping("/cart/{user}")
    public ResponseEntity getCartByUser(@PathVariable (value = "user") User user) {
        try{
            Cart cart= purchasingService.getCart(user);
            if(cart.getStoredProductsInCart().size()<=0){
                return new ResponseEntity(new ResponseMessage("The cart is empty!",cart),HttpStatus.OK);
            }
            return new ResponseEntity(cart,HttpStatus.OK);
        }catch (CartNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }


}
