package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.controllers.rest.StoredProduct;
import it.carmelogug.puntoinformatica.entities.Product;
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

    @Transactional(readOnly = false)
    public void addStoredProduct(StoredProduct storedProduct) throws StoredProductAlreadyExistException, ProductNotExistException, StoreNotExistException {


    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts() {
        return null;
    }
}
