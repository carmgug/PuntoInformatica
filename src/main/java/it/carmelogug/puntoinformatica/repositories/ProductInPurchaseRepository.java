package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.purchasing.ProductInPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase, Integer> {

}