package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.purchasing.StoredProductInPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredProductInPurchaseRepository extends JpaRepository<StoredProductInPurchase, Integer> {

}