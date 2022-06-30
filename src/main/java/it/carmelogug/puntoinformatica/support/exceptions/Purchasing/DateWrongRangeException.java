package it.carmelogug.puntoinformatica.support.exceptions.Purchasing;

public class DateWrongRangeException extends Exception{

    private final static String message="startDate must be previous endDate";

    public DateWrongRangeException () {
        super(message);
    }


}
