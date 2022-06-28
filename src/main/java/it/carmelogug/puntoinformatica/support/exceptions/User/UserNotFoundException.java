package it.carmelogug.puntoinformatica.support.exceptions.User;

public class UserNotFoundException extends Exception{

    private final static String message="User not found!";

    public UserNotFoundException() {
        super(message);
    }
}
