package it.carmelogug.puntoinformatica.repositories;




import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.Store;

import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredProductRepository extends JpaRepository<StoredProduct,Integer> {

    StoredProduct findStoredProductById(int id);
    boolean existsByStoreAndProduct(Store store, Product product);


    @Query("SELECT sp " +
            "FROM StoredProduct AS sp " +
            "WHERE  (sp.store = :store ) AND " +
            "       (sp.product = :p) AND " +
            "       (sp.price <= :price OR :price IS NULL ) AND " +
            "       (sp.quantity > :quantity OR :quantity IS NULL )")
    List<StoredProduct> advSearchByStoreAndProductAndPriceAndQuantity(Store store, Product p, Double price, Integer quantity);
}
