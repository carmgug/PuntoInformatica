
/*
package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.StoredProduct;
import it.carmelogug.puntoinformatica.repositories.ProductRepository;
import it.carmelogug.puntoinformatica.repositories.StoreRepository;
import it.carmelogug.puntoinformatica.repositories.StoredProductRepository;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Store.StoreNotExistException;
import it.carmelogug.puntoinformatica.support.exceptions.StoredProduct.StoredProductAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoredProductService {

    @Autowired
    private StoredProductRepository storedProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Transactional(readOnly = false)
    public void addStoredProduct(StoredProduct storedProduct) throws StoredProductAlreadyExistException, ProductNotExistException, StoreNotExistException {
        if(!productRepository.existsById(storedProduct.getProduct().getId())){
            throw new ProductNotExistException();
        }
        if(!storeRepository.existsById(storedProduct.getStore().getId())){
            throw new StoreNotExistException();
        }
        if(storedProductRepository.existsByStoreAndProduct(storedProduct.getStore(),storedProduct.getProduct())){
            throw new StoredProductAlreadyExistException();
        }
        storedProductRepository.save(storedProduct);
    }

    @Transactional(readOnly = true)
    public List<StoredProduct> showAllStoredProducts() {
        return storedProductRepository.findAll();
    }

}
*/
