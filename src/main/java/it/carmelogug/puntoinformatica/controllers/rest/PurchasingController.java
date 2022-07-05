package it.carmelogug.puntoinformatica.controllers.rest;

import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import it.carmelogug.puntoinformatica.entities.purchasing.Purchase;
import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.entities.purchasing.StoredProductInCart;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import it.carmelogug.puntoinformatica.services.PurchasingService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.authentication.Utils;
import it.carmelogug.puntoinformatica.support.exceptions.Purchasing.*;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.User.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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







    /*
        Metodi per la gestione del carrello
     */




    @PostMapping("/cart")
    public ResponseEntity createCart(@PathVariable (value = "user") User user){
        try {
            Cart cart = purchasingService.addCart(user);
            return new ResponseEntity(new ResponseMessage("Cart created successfully!",cart),HttpStatus.OK);
        }catch (UserNotFoundException | CartAlreadyExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }

    /*
        Metodi utilizzati dal frontEnd
     */
    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @PutMapping("/cart/my_cart/addStoredProduct")
    public ResponseEntity addStoredProductToCart(@RequestBody StoredProduct storedProduct,
                                                  @RequestParam (value = "quantity") int quantity){
        try{
            if(quantity==0) throw new QuantityWrongException();
            Cart cart = purchasingService.addStoredProductToCart(Utils.getEmail(),storedProduct,quantity);
            return new ResponseEntity(new ResponseMessage("StoredProduct added successfully!",cart),HttpStatus.OK);
        }catch (UserNotFoundException | CartNotExistException | StoredProductNotExistException | QuantityWrongException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }

    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @PutMapping("/cart/my_cart/modifyQuantityStoredProductInCart")
    public ResponseEntity modifyQuantityStoredProduct(@RequestBody StoredProductInCart storedProductInCart,
                                                 @RequestParam (value = "quantity") int quantity) {
        try{
            if(quantity<0) throw new QuantityWrongException();
            System.out.println(quantity);
            Cart cart = purchasingService.modifyquantityStoredProductInCart(storedProductInCart,quantity);
            return new ResponseEntity(new ResponseMessage("Product updated successfully!",cart),HttpStatus.OK);
        }catch ( StoredProductNotInCart| QuantityWrongException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }


    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @DeleteMapping("/cart/my_cart/removeStoredProductInCart")
    public ResponseEntity removeStoredProductFromCart(@RequestBody @Valid StoredProductInCart storedProductInCart){

        try{
            Cart cart= purchasingService.removeStoredProductFromCart(Utils.getEmail(),storedProductInCart);
            return new ResponseEntity(new ResponseMessage("Product has been deleted from the cart!",cart),HttpStatus.OK);
        } catch (StoredProductNotInCart| CartNotExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }

    }

    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @GetMapping("/cart/my_cart")
    public ResponseEntity getCartByUser() {
        try{
            Cart cart= purchasingService.getCart(Utils.getEmail());
            if(cart.getStoredProductsInCart().size()<=0){
                return new ResponseEntity(new ResponseMessage("The cart is empty!",cart),HttpStatus.OK);
            }
            return new ResponseEntity(new ResponseMessage("ok",cart),HttpStatus.OK);
        }catch (CartNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }


    /*
        startDate e endDate possono essere null, in tal caso vengono restituiti tutti gli acquisti dell'utente loggato.
     */
    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @GetMapping("/purchases/by_period")
    public ResponseEntity getPurchasesInPeriod(
            @RequestParam (value = "startDate",required = false) @DateTimeFormat(pattern = "MM-dd-yyyy") Date startDate,
            @RequestParam (value = "endDate",required = false) @DateTimeFormat(pattern = "MM-dd-yyyy") Date endDate) {

        try {
            List<Purchase>result = purchasingService.getPurchasesByUserInPeriod(Utils.getEmail(), startDate, endDate);
            if (result.size() <= 0) {
                return new ResponseEntity(new ResponseMessage("No result!",result), HttpStatus.OK);
            }
            return new ResponseEntity(new ResponseMessage("Purchases founded",result),HttpStatus.OK);

        }catch(UserNotFoundException | DateWrongRangeException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }

    @PostMapping("/purchase/{cart}")
    public ResponseEntity createPurchase(@PathVariable(value = "cart") Cart cart) {
        try{
            Purchase result=purchasingService.addPurchase(cart);
            return new ResponseEntity(new ResponseMessage(
                    "Purchase order has been processed!",result)
                    ,HttpStatus.OK);
        }catch (QuantityProductUnvailableException | CartIsEmptyException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }


}
