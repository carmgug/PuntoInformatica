package it.carmelogug.puntoinformatica.support.exceptions.Product;

public class ProductNotExistException extends Exception{

    private final static String message="Product does not exist!";
    public ProductNotExistException() {
        super(message);
    }
}
