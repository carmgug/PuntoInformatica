package it.carmelogug.puntoinformatica.support.exceptions.Product;

public class ProductIsBannedException extends Exception{

    private final static String message="The product cannot be sold";

    public ProductIsBannedException(){
        super(message);
    }
}
