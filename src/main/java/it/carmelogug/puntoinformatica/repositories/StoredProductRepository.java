package it.carmelogug.puntoinformatica.repositories;


import it.carmelogug.puntoinformatica.controllers.rest.StoredProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredProductRepository extends JpaRepository<StoredProduct,Integer> {

}
