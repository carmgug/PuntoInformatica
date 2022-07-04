package it.carmelogug.puntoinformatica.support.exceptions.Purchasing;

public class QuantityWrongException extends Exception{
    private final static String message="Quantity is not valid";

    public QuantityWrongException(){
        super(message);
    }

}
