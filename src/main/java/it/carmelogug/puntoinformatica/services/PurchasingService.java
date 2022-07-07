package it.carmelogug.puntoinformatica.services;

import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import it.carmelogug.puntoinformatica.entities.purchasing.StoredProductInCart;
import it.carmelogug.puntoinformatica.entities.purchasing.ProductInPurchase;
import it.carmelogug.puntoinformatica.entities.purchasing.Purchase;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.repositories.*;
import it.carmelogug.puntoinformatica.support.exceptions.Purchasing.*;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.User.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

@Service
public class PurchasingService {




    private PurchaseRepository purchaseRepository;

    private ProductInPurchaseRepository productInPurchaseRepository;

    private CartRepository cartRepository;


    private UserRepository userRepository;


    private StoredProductInCartRepository storedProductInCartRepository;

    @Autowired
    public PurchasingService(PurchaseRepository purchaseRepository, ProductInPurchaseRepository productInPurchaseRepository,
                             CartRepository cartRepository, UserRepository userRepository, StoredProductInCartRepository storedProductInCartRepository){
        this.purchaseRepository = purchaseRepository;
        this.productInPurchaseRepository = productInPurchaseRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.storedProductInCartRepository = storedProductInCartRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    /*
        Gestione acquisti
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW, rollbackFor = QuantityProductUnvailableException.class)
    public Purchase addPurchase(Cart cart) throws QuantityProductUnvailableException, CartIsEmptyException {
        entityManager.refresh(cart);
        entityManager.lock(cart,LockModeType.OPTIMISTIC);
        if(cart.getStoredProductsInCart().size()==0) throw new CartIsEmptyException();

        Purchase result=purchaseRepository.save(new Purchase());
        double totalPrice=0;
        for(StoredProductInCart currp: cart.getStoredProductsInCart()){
            StoredProduct storedProduct=currp.getStoredProduct();

            entityManager.lock(storedProduct, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            int newQuantity=storedProduct.getQuantity()-currp.getQuantity();

            if ( newQuantity<0){
                throw new QuantityProductUnvailableException(
                        "Quantity Product Unvailable!"+","+
                                "Product: " + storedProduct.getId()+","+
                                "Available: " + storedProduct.getQuantity()
                );
            }
            storedProduct.setQuantity(newQuantity);

            ProductInPurchase pip=new ProductInPurchase();
            pip.setQuantity(currp.getQuantity());
            pip.setPrice(currp.getQuantity()*storedProduct.getPrice());
            pip.setStore(storedProduct.getStore());
            pip.setProduct(storedProduct.getProduct());
            pip.setPurchase(result);
            productInPurchaseRepository.save(pip);//insert record in database
            storedProductInCartRepository.delete(currp); //element has been sold
            totalPrice+= pip.getPrice();
        }
        entityManager.refresh(result);
        result.setBuyer(cart.getBuyer());
        result.setPrice(totalPrice);
        result=entityManager.merge(result);//update record in database
        return result;
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Purchase> getPurchasesByUserInPeriod(String email, Date startDate, Date endDate) throws UserNotFoundException, DateWrongRangeException {

        if(endDate!=null) {
            Calendar c=Calendar.getInstance();
            c.setTime(endDate);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.HOUR_OF_DAY, 23);
            endDate=c.getTime();
        }

        if(startDate==null) startDate=new Date(0); //1970-01-01
        if(endDate==null) endDate=new Date(System.currentTimeMillis());

        if(startDate.after(endDate)) throw new DateWrongRangeException();


        User currUser=userRepository.findUserByEmail(email);
        if(currUser==null) throw new UserNotFoundException();
        List<Purchase> result= purchaseRepository.getPurchasesByBuyerAndPurchaseTimeBetweenStartDateAndEndDate(currUser,startDate,endDate);

        return result;
    }


    /*
        Gestione Carrello
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Cart addCart(User user) throws UserNotFoundException, CartAlreadyExistException {
        User currUser=entityManager.find(User.class,user.getId());
        if(currUser==null) throw new UserNotFoundException();
        if(cartRepository.existsByBuyer(currUser)) throw new CartAlreadyExistException();

        Cart cart=new Cart();
        cart.setBuyer(currUser);
        cart=cartRepository.save(cart);
        return cart;
    }
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Cart getCart(String email) throws CartNotExistException {
        User currUser=userRepository.findUserByEmail(email);
        Cart currCart=cartRepository.findCartByBuyer(currUser);
        if(currCart==null) throw new CartNotExistException();
        return currCart;

    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Cart addStoredProductToCart(String email,StoredProduct storedProduct,int quantity) throws UserNotFoundException, StoredProductNotExistException, CartNotExistException {

        User currUser=userRepository.findUserByEmail(email);
        if(currUser==null) throw new UserNotFoundException();

        storedProduct=entityManager.find(StoredProduct.class,storedProduct.getId());
        if(storedProduct==null) throw new StoredProductNotExistException();

        Cart currCart=cartRepository.findCartByBuyer(currUser);
        if(currCart==null) throw new CartNotExistException();


        StoredProductInCart addedElement=storedProductInCartRepository.findStoredProductInCartByCartAndStoredProduct(currCart,storedProduct);
        if(addedElement!=null) {
            int newQuantity= addedElement.getQuantity()+quantity;
            addedElement.setQuantity(newQuantity);
        }
        else {
            addedElement=new StoredProductInCart();
            addedElement.setCart(currCart);
            addedElement.setStoredProduct(storedProduct);
            addedElement.setQuantity(quantity);
        }
        storedProductInCartRepository.save(addedElement);
        entityManager.refresh(currCart);
        return currCart;
    }

    @Transactional(readOnly = false)
    public Cart removeStoredProductFromCart(String email,StoredProductInCart storedProductInCart) throws CartNotExistException, StoredProductNotInCart {
        StoredProductInCart spic=storedProductInCartRepository.findStoredProductInCartById(storedProductInCart.getId());
        if(storedProductInCart==null) throw new StoredProductNotInCart();
        storedProductInCartRepository.delete(spic);
        //restituisco il carrello aggiornato
        User currUser=userRepository.findUserByEmail(email);
        Cart currCart=cartRepository.findCartByBuyer(currUser);
        return currCart;
    }


    public Cart updateQuantityStoredProductInCart(StoredProductInCart storedProductInCart, int quantity) throws StoredProductNotInCart {
        StoredProductInCart spic=storedProductInCartRepository.findStoredProductInCartById(storedProductInCart.getId());
        if(storedProductInCart==null) throw new StoredProductNotInCart();
        spic.setQuantity(quantity);
        spic=storedProductInCartRepository.save(spic);
        return spic.getCart();

    }
}
