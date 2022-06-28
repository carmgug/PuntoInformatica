package it.carmelogug.puntoinformatica.services;

import it.carmelogug.puntoinformatica.entities.ProductInPurchase;
import it.carmelogug.puntoinformatica.entities.Purchase;
import it.carmelogug.puntoinformatica.entities.StoredProduct;
import it.carmelogug.puntoinformatica.repositories.ProductInPurchaseRepository;
import it.carmelogug.puntoinformatica.repositories.PurchaseRepository;
import it.carmelogug.puntoinformatica.support.exceptions.Purchase.QuantityProductUnvailableException;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

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
                        "Quantity Product Unvailable!"+"CR"+
                        "Product: " + storedProduct.getProduct().toString()+"CR"+
                        "Avaiable: " + storedProduct.getQuantity()
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
}
