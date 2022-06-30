package it.carmelogug.puntoinformatica.support.exceptions.Purchasing;

public class CartNotExistException extends Exception{
    private final static String message="The selected Cart doesn't exist!";

    public CartNotExistException(){
        super(message);
    }
}
