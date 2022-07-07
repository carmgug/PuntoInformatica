package it.carmelogug.puntoinformatica.support.exceptions.Store;

public class StoreNotExistException extends Exception{

    private final static String message="Store does not exist!";

    public StoreNotExistException() {
        super(message);
    }
}
