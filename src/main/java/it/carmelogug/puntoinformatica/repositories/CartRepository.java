package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    Cart findCartByBuyer(User buyer);
    boolean existsByBuyer(User buyer);
}
