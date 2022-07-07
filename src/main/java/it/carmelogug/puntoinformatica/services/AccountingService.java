package it.carmelogug.puntoinformatica.services;

import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import it.carmelogug.puntoinformatica.repositories.CartRepository;
import it.carmelogug.puntoinformatica.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountingService {


    private UserRepository userRepository;


    private CartRepository cartRepository;
    @Autowired
    public AccountingService(UserRepository userRepository, CartRepository cartRepository){
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional(readOnly = false, isolation= Isolation.READ_COMMITTED)
    public User addAndgetUser(String email,String first_name,String last_name){
        User user=userRepository.findUserByEmail(email);
        if(user==null){
            user=new User(email,first_name,last_name);
            user=userRepository.save(user);
            //Ogni volta che viene creato un utente, creo il carrello associato
            Cart cart=new Cart();
            cart.setBuyer(user);
            cartRepository.save(cart);
        }
        return user;
    }





}
