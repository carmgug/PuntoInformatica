package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import it.carmelogug.puntoinformatica.entities.purchasing.StoredProductInCart;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredProductInCartRepository extends JpaRepository<StoredProductInCart,Integer> {


    StoredProductInCart findStoredProductInCartByCartAndStoredProduct(Cart cart, StoredProduct storedProduct);

}
