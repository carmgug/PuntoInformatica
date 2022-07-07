package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.Store;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import it.carmelogug.puntoinformatica.repositories.ProductRepository;
import it.carmelogug.puntoinformatica.repositories.StoreRepository;
import it.carmelogug.puntoinformatica.repositories.StoredProductRepository;
import it.carmelogug.puntoinformatica.support.Utilities;


import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductIsBannedException;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreIsBannedException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductNotExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import java.util.List;


@Service
public class StoreService {


    private StoreRepository storeRepository;


    private StoredProductRepository storedProductRepository;


    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public StoreService(StoreRepository storeRepository, StoredProductRepository storedProductRepository, ProductRepository productRepository) {
        this.storeRepository = storeRepository;
        this.storedProductRepository = storedProductRepository;
        this.productRepository = productRepository;
    }


    /*
        +++Methods for managing stores+++
    */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Store addStore(Store store) throws StoreAlreadyExistException{
        Utilities.adjustPropreties(store);
        if(storeRepository.existsByCountryAndRegionAndCityAndProvinceAndAddress(
                store.getCountry(),store.getRegion(),
                store.getCity(),store.getProvince(),
                store.getAddress() )){
            throw new StoreAlreadyExistException();
        }
        store=storeRepository.save(store);
        return store;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Store banStore(Store store) throws StoreNotExistException {
        Store currStore=storeRepository.findStoreById(store.getId());
        if(currStore==null) throw new StoreNotExistException();
        if(currStore.getStoredProducts()!=null && currStore.getStoredProducts().size()>0)

        for(StoredProduct sp:currStore.getStoredProducts()){
            entityManager.lock(sp,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            storedProductRepository.delete(sp);
        }
        currStore.setBanned(true);
        return currStore;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Store unbanStore(Store store) throws StoreNotExistException {
        Store currStore=storeRepository.findStoreById(store.getId());
        if(currStore==null) throw new StoreNotExistException();
        currStore.setBanned(false);
        return currStore;
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Store> showStoresByCountryAndRegionAndCityAndProvinceAndAddress(String country, String region, String city,String province, String address) {
        return storeRepository.advSearchByCountryAndRegionAndCityAndProvinceAndAddress(
                Utilities.upperCase(country,false),
                Utilities.upperCase(region,false),
                Utilities.upperCase(city,false),
                Utilities.upperCase(province,false),
                Utilities.upperCase(address,false));
    }//showStoresByCountryAndRegionAndCityAndProvinceAndAddress




    /*
        +++Methods for managing stored products+++
    */
    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public StoredProduct addStoredProduct(StoredProduct storedProduct) throws StoredProductAlreadyExistException, ProductIsBannedException, StoreIsBannedException, ProductNotExistException, StoreNotExistException {
        if(storedProduct.getStore()==null) throw new StoreNotExistException();
        if(storedProduct.getProduct()==null) throw new ProductNotExistException();
        if(storedProduct.getStore().isBanned()) throw new StoreIsBannedException();
        if(storedProduct.getProduct().isBanned()) throw new ProductIsBannedException();

        if(storedProductRepository.existsByStoreAndProduct(storedProduct.getStore(),storedProduct.getProduct()))
            throw new StoredProductAlreadyExistException();
        storedProduct=storedProductRepository.save(storedProduct);
        return storedProduct;
    }//addStoredProduct


    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public StoredProduct updateStoredProduct(Store store, Product product, Integer quantity, Double price) throws StoredProductNotExistException {
        if(quantity==null && price==null){
            throw new RuntimeException("Quantity and Price must not be null at the same time during an update");
        }
        StoredProduct currSP=storedProductRepository.findStoredProductByStoreAndProduct(store,product);
        if(currSP==null){
            throw new StoredProductNotExistException();
        }
        entityManager.lock(currSP,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        if(quantity!=null) currSP.setQuantity(quantity);
        if(price!=null) currSP.setPrice(price);
        return currSP;
    }//updateStoredProduct




    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public StoredProduct removeStoredProduct(Store store,Product product) throws StoredProductNotExistException{
        StoredProduct currStoredProduct=storedProductRepository.findStoredProductByStoreAndProduct(store,product);
        if(currStoredProduct==null) throw new StoredProductNotExistException();
        entityManager.lock(currStoredProduct,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        storedProductRepository.delete(currStoredProduct);
        return currStoredProduct;
    }//removeStoredProduct




    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<StoredProduct> showStoredProductsByStoreAndProductAndPriceAndQuantity(
            Store store,
            int product_id,
            Double price,Integer quantity
    ){
        List<StoredProduct> result;
        Product product= productRepository.findProductById(product_id);

        System.out.println(product);
        result=storedProductRepository.advSearchByStoreAndProductAndPriceAndQuantity(store,product,price,quantity);

        return result;
    }//showSearchByStoreAndProductAndPriceAndQuantity





    /*
        NON UTILIZZATI
     */

    @Transactional(readOnly = true)
    public List<Store> showAllStores(){
        return storeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Store showStoreByStoreID(Integer storeID) throws StoreNotExistException {
        Store store=storeRepository.findStoreById(storeID);
        if(store==null) throw new StoreNotExistException();
        return store;
    }

    @Transactional(readOnly = true)
    public List<StoredProduct> showStoredProductsByStore(Store store){
        store=storeRepository.findStoreById(store.getId());
        return store.getStoredProducts();
    }



}
