package it.carmelogug.puntoinformatica.services;

import it.carmelogug.puntoinformatica.entities.ProductInPurchase;
import it.carmelogug.puntoinformatica.entities.Purchase;
import it.carmelogug.puntoinformatica.entities.StoredProduct;
import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.repositories.ProductInPurchaseRepository;
import it.carmelogug.puntoinformatica.repositories.PurchaseRepository;
import it.carmelogug.puntoinformatica.support.exceptions.Purchase.DateWrongRangeException;
import it.carmelogug.puntoinformatica.support.exceptions.Purchase.QuantityProductUnvailableException;
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
import java.sql.Time;
import java.time.format.DateTimeParseException;
import java.util.Date;

import java.util.List;
import java.util.Timer;

@Service
public class PurchasingService {



    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    ProductInPurchaseRepository productInPurchaseRepository;




    @PersistenceContext
    EntityManager entityManager;




    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW, rollbackFor = QuantityProductUnvailableException.class)
    public Purchase addPurchase(Purchase purchase) throws QuantityProductUnvailableException, StoredProductNotExistException {
        Purchase res=purchaseRepository.save(purchase);
        double totalPrice=0;
        for(ProductInPurchase pip: purchase.getProductsInPurchase()){
            StoredProduct storedProduct=pip.getStoredProduct();

            storedProduct=entityManager.find(StoredProduct.class,storedProduct.getId());
            if(storedProduct==null) throw new StoredProductNotExistException();

            entityManager.lock(storedProduct, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            int newQuantity=storedProduct.getQuantity()-pip.getQuantity();
            if ( newQuantity<0){
                throw new QuantityProductUnvailableException(
                        "Quantity Product Unvailable!"+","+
                        "Product: " + storedProduct.getProduct().toString()+","+
                        "Available: " + storedProduct.getQuantity()
                );
            }
            storedProduct.setQuantity(newQuantity);

            pip.setPrice(pip.getQuantity()* storedProduct.getPrice());
            pip.setStoredProduct(storedProduct);
            pip.setPurchase(res);
            productInPurchaseRepository.save(pip);//insert record in database

            totalPrice+= pip.getPrice();
        }
        entityManager.refresh(res);
        res.setPrice(totalPrice);
        res=entityManager.merge(res);//update record in database
        return res;
    }

    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByUserInPeriod(User user, Date startDate, Date endDate) throws UserNotFoundException, DateWrongRangeException {

        if(startDate!=null && endDate!=null && startDate.after(endDate)) throw new DateWrongRangeException();

        if(startDate==null) startDate=new Date(0); //1970-01-01
        if(endDate==null) endDate=new Date(System.currentTimeMillis());

        User currUser=entityManager.find(User.class,user.getId());
        if(currUser==null) throw new UserNotFoundException();
        List<Purchase> result= purchaseRepository.getPurchasesByBuyerAndPurchaseTimeBetweenStartDateAndEndDate(user,startDate,endDate);

        return result;
    }
}
