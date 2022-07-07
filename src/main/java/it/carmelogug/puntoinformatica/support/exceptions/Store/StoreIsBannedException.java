package it.carmelogug.puntoinformatica.support.exceptions.Store;

public class StoreIsBannedException extends Exception{

    private final static String message="Store cannot sell products ";

    public StoreIsBannedException(){
        super(message);
    }
}
