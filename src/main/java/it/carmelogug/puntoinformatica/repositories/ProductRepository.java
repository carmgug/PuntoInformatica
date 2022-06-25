package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {


    List<Product> findByUrl(String urlProducer);
    List<Product> findProductsByNameContaining(String name);

    Product getProductByBarCodeAndTypeAndCategory(long barCode,Product.Type type,Product.Category category);


    @Query("SELECT p " +
            "FROM Product as p " +
            "WHERE (upper(p.name) LIKE :name OR :name IS NULL ) AND " +
            "       (p.type = :type OR :type IS NULL ) AND " +
            "       (p.category = :category OR :category IS NULL )")
    List<Product> advSearchByNameAndTypeAndCategory(String name,Product.Type type,Product.Category category);

    /*
        La query sopra riassume le seguenti.
        List<Product> findProductsByType(Product.Type type);
        List<Product> findProductsByCategory(Product.Category category);
        List<Product> findProductsByTypeAndCategory(Product.Type type, Product.Category category);
     */

    boolean existsByBarCodeAndTypeAndCategory(long barCode, Product.Type type, Product.Category category);

}
