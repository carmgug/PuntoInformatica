package it.carmelogug.puntoinformatica.support.exceptions.StoredProduct;

public class StoredProductAlreadyExistException extends Exception{

    private final static String message="Questo prodotto è gia venduto all'interno dello store!";
    public StoredProductAlreadyExistException() {
        super(message);
    }
}
