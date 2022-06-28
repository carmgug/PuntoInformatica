package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.Purchase;
import it.carmelogug.puntoinformatica.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {

    @Query("SELECT p " +
            "FROM Purchase AS p " +
            "WHERE (p.buyer = :user) AND " +
            "       (p.purchaseTime >= :startDate ) AND " +
            "       (p.purchaseTime <= :endDate)" )
    List<Purchase> getPurchasesByBuyerAndPurchaseTimeBetweenStartDateAndEndDate(User user, Date startDate,Date endDate);

}
