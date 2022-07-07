package it.carmelogug.puntoinformatica.support.exceptions.StoredProduct;

public class StoredProductNotExistException extends Exception{

    private final static String message="StoredProduct does not exist in the store!";
    public StoredProductNotExistException() {
        super(message);
    }
}
