package it.carmelogug.puntoinformatica.support.exceptions.Purchasing;

public class CartIsEmptyException extends Exception{
    private final static String message="The cart is empty!";

    public CartIsEmptyException(){
        super(message);
    }
}
