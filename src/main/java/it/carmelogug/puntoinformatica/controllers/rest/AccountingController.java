package it.carmelogug.puntoinformatica.controllers.rest;




import it.carmelogug.puntoinformatica.support.ResponseMessage;
import it.carmelogug.puntoinformatica.support.authentication.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AccountingController {


    @GetMapping("/logged")
    @PreAuthorize("hasAuthority('puntoinformatica-user')")
    public ResponseEntity checkLogged() {
        System.out.println("sono loggato");
        return new ResponseEntity(new ResponseMessage("Tutto ok! sei loggato"+ Utils.getEmail(),Utils.getName()),HttpStatus.OK);
    }




}
