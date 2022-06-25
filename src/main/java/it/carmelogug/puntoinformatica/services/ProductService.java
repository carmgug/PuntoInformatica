package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.Product;
import it.carmelogug.puntoinformatica.repositories.ProductRepository;
import it.carmelogug.puntoinformatica.support.Utilities;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /*
        Metodi per l'aggiunta e l'eleminazione dei prodotti
     */
    @Transactional(readOnly = false)
    public void addProduct(Product product) throws ProductAlreadyExistException {
        if (productRepository.existsByBarCodeAndTypeAndCategory(product.getBarCode(),product.getType(),product.getCategory())){
            throw new ProductAlreadyExistException();
        }
        productRepository.save(product);
    }

    @Transactional(readOnly = false)
    public void removeProduct(long barCode,Product.Type type,Product.Category category) throws ProductNotExistException{
        Product p = productRepository.getProductByBarCodeAndTypeAndCategory(barCode, type, category);
        if(p==null) throw new ProductNotExistException();
        productRepository.delete(p);

    }






    /*
        Metodi per accedere ai prodotti esistenti.
     */
    @Transactional(readOnly = true)
    public List<Product> showAllProducts() {
        return  productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByName(String name) {
        return productRepository.findProductsByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByNameAndTypeAndCategory(String name,Product.Type type,Product.Category category){
        name= Utilities.upperCase(name,true);
        return productRepository.advSearchByNameAndTypeAndCategory(name,type,category);
    }




}
