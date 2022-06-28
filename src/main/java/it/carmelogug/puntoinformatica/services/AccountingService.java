package it.carmelogug.puntoinformatica.services;

import it.carmelogug.puntoinformatica.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountingService {

    @Autowired
    private UserRepository userRepository;

}
