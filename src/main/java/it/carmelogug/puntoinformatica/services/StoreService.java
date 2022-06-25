package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.Product;
import it.carmelogug.puntoinformatica.entities.Store;
import it.carmelogug.puntoinformatica.repositories.StoreRepository;
import it.carmelogug.puntoinformatica.support.Utilities;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

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

    public Store showStoreByStoreID(Integer storeID) throws StoreNotExistException {
        Store store=storeRepository.findStoreById(storeID);
        if(store==null) throw new StoreNotExistException();
        return store;
    }
}
