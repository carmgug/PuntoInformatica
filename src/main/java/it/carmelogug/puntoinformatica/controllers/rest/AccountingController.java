package it.carmelogug.puntoinformatica.controllers.rest;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AccountingController {


    @GetMapping("/logged")
    public ResponseEntity checkLogged() {
        System.out.println("sono loggato");
        return new ResponseEntity("Check status,",HttpStatus.OK);
    }




}
