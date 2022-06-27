package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {

}
