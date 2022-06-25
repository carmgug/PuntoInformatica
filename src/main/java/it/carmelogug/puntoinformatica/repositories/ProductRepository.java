package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findByUrl(String urlProducer);
    List<Product> getProductsByType(Product.Type type);
    List<Product> getProductsByCategory(Product.Category category);
    List<Product> getProductsByTypeAndCategory(Product.Type type, Product.Category category);


    boolean existsByBarCodeAndTypeAndCategory(long barCode, Product.Type type, Product.Category category);

}
