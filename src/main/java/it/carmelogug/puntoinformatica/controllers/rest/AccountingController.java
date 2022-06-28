package it.carmelogug.puntoinformatica.controllers.rest;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AccountingController {

    @GetMapping("/status")
    public ResponseEntity checkStatus() {return new ResponseEntity("Check status ok!", HttpStatus.OK);}



}
