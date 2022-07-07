package it.carmelogug.puntoinformatica.services;


import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import it.carmelogug.puntoinformatica.repositories.ProductRepository;
import it.carmelogug.puntoinformatica.repositories.StoredProductRepository;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductAlreadyExistException;
import it.carmelogug.puntoinformatica.support.exceptions.Product.ProductNotExistException;

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
public class ProductService {

    private ProductRepository productRepository;


    private StoredProductRepository storedProductRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, StoredProductRepository storedProductRepository) {
        this.productRepository = productRepository;
        this.storedProductRepository = storedProductRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;





    /*
        Metodi per l'aggiunta e l'eliminazione dei prodotti
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Product addProduct(Product product) throws ProductAlreadyExistException {
        if (productRepository.existsByBarCodeAndTypeAndCategory(product.getBarCode(),product.getType(),product.getCategory())){
            throw new ProductAlreadyExistException();
        }
        Product result=productRepository.save(product);
        return result;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Product banProduct(Product product) throws ProductNotExistException{
        Product currProduct = entityManager.find(Product.class,product.getId());
        if(currProduct==null) throw new ProductNotExistException();


        for(StoredProduct sp:currProduct.getStoredProducts()){
            entityManager.lock(sp,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            storedProductRepository.delete(sp);
        }
        currProduct.setBanned(true);
        return currProduct; //ritorno l'oggetto rimosso
    }

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public Product unBanProduct(Product product) throws ProductNotExistException {
        Product currProduct = entityManager.find(Product.class,product.getId());
        if(currProduct==null) throw new ProductNotExistException();
        currProduct.setBanned(false);
        return currProduct; //ritorno l'oggetto rimosso
    }

    @Transactional(readOnly = true)
    public Product searchProductById(int id) throws ProductNotExistException {
        Product result=productRepository.findProductById(id);
        if(result==null){
            throw new ProductNotExistException();
        }
        return result;
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
        return productRepository.advSearchByNameAndTypeAndCategory(name,type,category);
    }



}
