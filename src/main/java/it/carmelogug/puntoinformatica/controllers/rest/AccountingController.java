package it.carmelogug.puntoinformatica.controllers.rest;




import it.carmelogug.puntoinformatica.entities.User;
import it.carmelogug.puntoinformatica.services.AccountingService;
import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.authentication.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AccountingController {


    @Autowired
    private AccountingService accountingService;

    /*
        Colui che richiama il metodo si è autenticato tramite il sito angular
        Quindi verifico che sia autorizzato e restituisco l'utente sul db se esiste
        se non esiste lo creo e lo restituisco.
     */
    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    @GetMapping("/logged")
    public ResponseEntity checkLogged() {
        System.out.println("sono loggato");

        String email=Utils.getEmail();
        String[] name=Utils.getName();
        String first_name=name[0];
        String last_name=name[1];
        User currUser=accountingService.addAndgetUser(email,first_name,last_name);
        return new ResponseEntity(new ResponseMessage("You are logged",currUser),HttpStatus.OK);
    }




}
