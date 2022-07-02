package it.carmelogug.puntoinformatica.services;

import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import it.carmelogug.puntoinformatica.repositories.CartRepository;
import it.carmelogug.puntoinformatica.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class AccountingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;




    @Transactional(readOnly = false)
    public User addAndgetUser(String email,String first_name,String last_name,String phonenumber){
        User user=userRepository.findUserByEmail(email);
        if(user==null){
            user=new User(email,first_name,last_name,phonenumber);
            user=userRepository.save(user);
            //Ogni volta che viene creato un utente, creo il carrello associato
            Cart cart=new Cart();
            cart.setBuyer(user);
            cartRepository.save(cart);
        }
        return user;
    }





}
