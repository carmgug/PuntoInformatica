package it.carmelogug.puntoinformatica.support.exceptions.Purchasing;

public class CartAlreadyExistException extends Exception{

    private final static String message= "The shopping cart for the current user already exists";

    public CartAlreadyExistException() {
        super(message);
    }

}
