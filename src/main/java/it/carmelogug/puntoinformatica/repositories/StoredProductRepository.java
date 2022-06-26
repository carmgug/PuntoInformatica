package it.carmelogug.puntoinformatica.repositories;



import it.carmelogug.puntoinformatica.entities.Product;
import it.carmelogug.puntoinformatica.entities.Store;
import it.carmelogug.puntoinformatica.entities.StoredProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredProductRepository extends JpaRepository<StoredProduct,Integer> {


    boolean existsByStoreAndProduct(Store store, Product product);
}
