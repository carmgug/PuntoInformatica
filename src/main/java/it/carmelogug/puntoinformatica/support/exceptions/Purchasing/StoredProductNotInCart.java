package it.carmelogug.puntoinformatica.support.exceptions.Purchasing;

public class StoredProductNotInCart extends Exception{
    private static String message="The product isn't in the current Cart";
    public StoredProductNotInCart() {
        super(message);
    }
}
