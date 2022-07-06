package it.carmelogug.puntoinformatica.support.exceptions.StoredProduct;

public class StoredProductNotExistException extends Exception{

    private final static String message="Lo storedProduct non esiste all'interno dello store!";
    public StoredProductNotExistException() {
        super(message);
    }
}
