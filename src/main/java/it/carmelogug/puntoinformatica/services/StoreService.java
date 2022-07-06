package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.Store;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import it.carmelogug.puntoinformatica.repositories.ProductRepository;
import it.carmelogug.puntoinformatica.repositories.StoreRepository;
import it.carmelogug.puntoinformatica.repositories.StoredProductRepository;
import it.carmelogug.puntoinformatica.support.Utilities;


import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreAlreadyExistException;
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

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoredProductRepository storedProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;


    /*
        +++Methods for managing stores+++
    */

    @Transactional(readOnly = false)
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

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public Store banStore(Store store) throws StoreNotExistException {
        Store currStore=storeRepository.findStoreById(store.getId());
        if(currStore==null) throw new StoreNotExistException();
        for(StoredProduct sp:currStore.getStoredProducts()){
            entityManager.lock(sp,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
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

    @Transactional(readOnly = true)
    public List<Store> showStoresByCountryAndRegionAndCityAndProvinceAndAddress(String country, String region, String city,String province, String address) {
        return storeRepository.advSearchByCountryAndRegionAndCityAndProvinceAndAddress(
                Utilities.upperCase(country,false),
                Utilities.upperCase(region,false),
                Utilities.upperCase(city,false),
                Utilities.upperCase(province,false),
                Utilities.upperCase(address,false));
    }//showStoresByCountryAndRegionAndCityAndProvinceAndAddress Utilizzato




    /*
        +++Methods for managing stored products+++
    */
    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public StoredProduct addStoredProduct(StoredProduct storedProduct) throws StoredProductAlreadyExistException {
        if(storedProductRepository.existsByStoreAndProduct(storedProduct.getStore(),storedProduct.getProduct()))
            throw new StoredProductAlreadyExistException();
        storedProduct=storedProductRepository.save(storedProduct);
        return storedProduct;
    }//addStoredProduct UTILIZZATO

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)

    public StoredProduct updateStoredProduct(Store store, Product product, Integer quantity, Double price) throws StoredProductNotExistException {
        if(quantity==null && price==null){
            throw new RuntimeException("Quantity and Price must not be null at the same time during an update");
        }
        StoredProduct currSP=storedProductRepository.findStoredProductByStoreAndProduct(store,product);
        if(currSP==null){
            throw new StoredProductNotExistException();
        }
        entityManager.lock(currSP,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        if(quantity!=null) currSP.setQuantity(quantity);
        if(price!=null) currSP.setPrice(price);
        return currSP;
    }//updateStoredProduct Utilizzato




    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW)
    public StoredProduct removeStoredProduct(Store store,Product product) throws StoredProductNotExistException{
        StoredProduct currStoredProduct=storedProductRepository.findStoredProductByStoreAndProduct(store,product);
        if(currStoredProduct==null) throw new StoredProductNotExistException();
        entityManager.lock(currStoredProduct,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        storedProductRepository.delete(currStoredProduct);
        return currStoredProduct;
    }//removeStoredProduct Utilizzato

    @Transactional(readOnly = true)
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

    private boolean existProduct(StoredProduct storedProduct){
        Product product=storedProduct.getProduct();

        //se mi è stato fornito l'id in input utilizzo quello per verificare l'esistenza del prodotto.
        if(product.getId()!=null && productRepository.existsById(product.getId()) ){
            return true;
        }
        //se non mi è stato fornito l'id e se la ricerca precedente ha fallito allora effettuo la ricerca
        //per barCode tipo e categoria
        else if (product.getBarCode()!=null && product.getType()!= null && product.getCategory()!=null){
            Product tmp=productRepository.getProductByBarCodeAndTypeAndCategory(product.getBarCode(),product.getType(),product.getCategory());
            if(tmp!=null){ //Il product in input esiste
                storedProduct.setProduct(tmp);
                return true;
            }
            return false;
        }
        //se non mi è stato passato nessuno dei parametri precedenti allora restituisco false
        return false;
    }//existProduct

    private boolean existStore( StoredProduct storedProduct){
        Store store=storedProduct.getStore();
        if(store.getId()!=null && storeRepository.existsById(store.getId())){
            return true;
        }
        //Se non mi è stato fornito l'id e se la ricerca precedente ha fallito allora effettuo la ricerca
        //Per country,region,city,province e address (che insieme identificano un negozio)
        else if(store.getCountry()!=null && store.getRegion()!= null &&
                store.getCity()!=null && store.getProvince()!=null && store.getAddress()!=null){
            //La ricerca deve essere CaseInsensitive
            Utilities.adjustPropreties(store);
            Store tmp=storeRepository.findByCountryAndRegionAndCityAndProvinceAndAddress(
                    store.getCountry(),store.getRegion(),store.getCity(),store.getProvince(),store.getAddress()
            );
            if(tmp!=null) {
                storedProduct.setStore(tmp);
                return true;
            }
            return false;
        }
        return false;
    }//existStore

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public StoredProduct updateQuantityStoredProduct(StoredProduct storedProduct,Integer quantity) throws StoredProductNotExistException {


        storedProduct=entityManager.find(StoredProduct.class,storedProduct.getId());
        if(storedProduct==null) throw new StoredProductNotExistException();

        entityManager.lock(storedProduct,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        storedProduct.setQuantity(storedProduct.getQuantity()+quantity);
        storedProductRepository.save(storedProduct);

        return storedProduct;
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public StoredProduct updatePriceStoredProduct(StoredProduct storedProduct,Double price) throws StoredProductNotExistException{
        storedProduct=entityManager.find(StoredProduct.class,storedProduct.getId());
        if(storedProduct==null) throw new StoredProductNotExistException();

        entityManager.lock(storedProduct,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        storedProduct.setPrice(price);
        storedProductRepository.save(storedProduct);

        return storedProduct;
    }

    @Transactional(readOnly = true)
    public List<StoredProduct> showStoredProductsByStore(Store store){
        store=storeRepository.findStoreById(store.getId());
        return store.getStoredProducts();
    }



}
