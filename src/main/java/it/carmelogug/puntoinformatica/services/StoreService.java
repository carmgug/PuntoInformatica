package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.Product;
import it.carmelogug.puntoinformatica.entities.Store;
import it.carmelogug.puntoinformatica.entities.StoredProduct;
import it.carmelogug.puntoinformatica.repositories.ProductRepository;
import it.carmelogug.puntoinformatica.repositories.StoreRepository;
import it.carmelogug.puntoinformatica.repositories.StoredProductRepository;
import it.carmelogug.puntoinformatica.support.Utilities;

import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;
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


import java.util.LinkedList;
import java.util.List;


@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoredProductRepository storedProductRepository;

    @Autowired
    private ProductRepository productRepository;


    /*
        The persistence context contains entity instances and used to manage the entity instances life cycle.
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = false)
    public Store addStore(Store store) throws StoreAlreadyExistException{
        if(storeRepository.existsByCountryAndRegionAndCityAndProvinceAndAddress(
                store.getCountry(),store.getRegion(),
                store.getCity(),store.getProvince(),
                store.getAddress() )){
            throw new StoreAlreadyExistException();
        }
        Utilities.adjustPropreties(store);
        store=storeRepository.save(store);
        return store;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Store removeStore(Store store) throws StoreNotExistException {
        Store s=storeRepository.findStoreById(store.getId());
        if(s==null) throw new StoreNotExistException();
        storeRepository.delete(s);
        return s;
    }

    @Transactional(readOnly = true)
    public List<Store> showAllStores(){
        return storeRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Store> showStoresByCountryAndRegionAndCityAndProvinceAndAddress(String country, String region, String city,String province, String address) {
        return storeRepository.advSearchByCountryAndRegionAndCityAndProvinceAndAddress(
                Utilities.upperCase(country,false),
                Utilities.upperCase(region,false),
                Utilities.upperCase(city,false),
                Utilities.upperCase(province,false),
                Utilities.upperCase(address,false));

    }

    @Transactional(readOnly = true)
    public Store showStoreByStoreID(Integer storeID) throws StoreNotExistException {
        Store store=storeRepository.findStoreById(storeID);
        if(store==null) throw new StoreNotExistException();
        return store;
    }


    /*
        +++Methods for managing stored products+++
    */
    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public StoredProduct addStoredProduct(StoredProduct storedProduct) throws ProductNotExistException, StoreNotExistException, StoredProductAlreadyExistException {
        //Verifichiamo che il product esista
        if(!existProduct(storedProduct))
            throw new ProductNotExistException();
        if(!existStore(storedProduct))
            throw new StoreNotExistException();
        if(storedProductRepository.existsByStoreAndProduct(storedProduct.getStore(),storedProduct.getProduct()))
            throw new StoredProductAlreadyExistException();

        storedProduct=storedProductRepository.save(storedProduct);
        return storedProduct;
    }//addStoredProduct

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

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW)
    public StoredProduct removeStoredProduct(StoredProduct storedProduct) throws StoredProductNotExistException{
        storedProduct=storedProductRepository.findStoredProductById(storedProduct.getId());
        if(storedProduct==null) throw new StoredProductNotExistException();
        storedProductRepository.delete(storedProduct);
        return storedProduct;
    }//removeStoredProduct


    /*
        https://www.baeldung.com/jpa-optimistic-locking Per capire le lock.
     */
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



    //Per gestire le impaginazioni
    //PageImpl(List<T> content, Pageable pageable, long total)
    //where
    //
    //content – the content of this page(Your collection object).
    //pageable – the paging information
    //total – the total amount of items available.
    @Transactional(readOnly = true)
    public List<StoredProduct> showStoredProductsByStoreAndProductAndPriceAndQuantity(
            Store store,
            String name,Product.Type type, Product.Category category,
            Double price,Integer quantity
    ){
        List<StoredProduct> result= new LinkedList<StoredProduct>();
        List<Product> products= productRepository.advSearchByNameAndTypeAndCategory(name,type,category);
        for(Product p: products){
            result.addAll(storedProductRepository.advSearchByStoreAndProductAndPriceAndQuantity(store,p,price,quantity));
        }

        return result;
    }//showSearchByStoreAndProductAndPriceAndQuantity



}
