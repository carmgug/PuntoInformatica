package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.Store;
import it.carmelogug.puntoinformatica.entities.StoredProduct;
import it.carmelogug.puntoinformatica.repositories.ProductRepository;
import it.carmelogug.puntoinformatica.repositories.StoreRepository;
import it.carmelogug.puntoinformatica.repositories.StoredProductRepository;
import it.carmelogug.puntoinformatica.support.Utilities;

import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.boot.autoconfigure.orm.*;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import java.util.List;

@PersistenceContext
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
    public void addStore(Store store) throws StoreAlreadyExistException{
        if(storeRepository.existsByCountryAndRegionAndCityAndProvinceAndAddress(
                store.getCountry(),store.getRegion(),
                store.getCity(),store.getProvince(),
                store.getAddress() )){

            throw new StoreAlreadyExistException();
        }
        storeRepository.save(store);
    }

    /*
        Metodo privato per effettuare una formattazione dei campi dello store
     */
    private void adjustPropreties(Store store){
        store.setCountry(Utilities.upperCase(store.getCountry(),false));
        store.setRegion(Utilities.upperCase(store.getRegion(),false));
        store.setProvince(Utilities.upperCase(store.getCity(),false));
        store.setCity(Utilities.upperCase(store.getProvince(),false));
        store.setAddress(Utilities.upperCase(store.getAddress(),false));
    }

    @Transactional(readOnly = false)
    public void removeStore(Store store) throws StoreNotExistException {
        if(!storeRepository.existsById(store.getId())) throw new StoreNotExistException();
        storeRepository.delete(store);
    }


    @Transactional(readOnly = true)
    public List<Store> showAllStores(){
        return storeRepository.findAll();
    }


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
        Metodi per la gestione dei prodotti venduti in uno store.
     */
    @Transactional(readOnly = false)
    public StoredProduct addStoredProduct(StoredProduct storedProduct){
        entityManager.lock(storedProduct, LockModeType.PESSIMISTIC_READ);
        return null;
    }







}
