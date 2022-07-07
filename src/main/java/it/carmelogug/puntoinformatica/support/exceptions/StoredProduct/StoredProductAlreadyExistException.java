package it.carmelogug.puntoinformatica.support.exceptions.StoredProduct;

public class StoredProductAlreadyExistException extends Exception{

    private final static String message="StoredProduct Already Exist!";
    public StoredProductAlreadyExistException() {
        super(message);
    }
}
