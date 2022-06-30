package it.carmelogug.puntoinformatica.support;

import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.Store;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;


public final class Utilities {

    /*
        Permette di restituire l'upperCase di una stringa, gestendo il caso anche in cui essa sia nulla.

        +++L'uppercase ci server per garantire il caseInsesitive nelle query.+++

        +++Se Containing è true allora str in input sarà formattata in modo tale da
        essere compatibile con Query che verificano che la str sia contenuta in un'altra str e non che sia esattamente
        uguale ad essa.+++
    */
    public static String upperCase(String str, boolean Containing) {
            if (str == null || str.isEmpty()) {
                return null;
            }
            if(Containing && !str.isBlank()){
                return "%"+str.toUpperCase()+"%";
            }
            return str.toUpperCase();
        }//upperCase


    /*
        Metodo utili per i product
     */
    public static Product.Type convertStringToType(String type){
            if(type == null){
                return null;
            }
            switch (type.toUpperCase()) {
                case "SOFTWARE":
                    return Product.Type.SOFTWARE;
                case "HARDWARE":
                    return Product.Type.HARDWARE;
                default:
                    return null;
            }
        }//convertStringToType

    public static Product.Category convertStringToCategory(String category){
        if(category == null){
            return null;
        }
        switch (category.toUpperCase()) {
            case "ACCESSORIES AND PERIPHERALS":
                return Product.Category.ACCESSORIESandPERIPHERALS;
            case "COMPONENT":
                return Product.Category.COMPONENT;
            case "COMPUTER":
                return Product.Category.COMPUTER;
            case "MAC":
                return Product.Category.MAC;
            case "MONITORS":
                return Product.Category.MONITORS;
            case "SMARTPHONE":
                return Product.Category.SMARTPHONE;
            default:
                return null;
        }
    }//convertStringToCategory


    /*
        Metodi utili per lo store
     */
    public static void adjustPropreties(Store store){
        store.setCountry(Utilities.upperCase(store.getCountry(),false));
        store.setRegion(Utilities.upperCase(store.getRegion(),false));
        store.setCity(Utilities.upperCase(store.getCity(),false));
        store.setProvince(Utilities.upperCase(store.getProvince(),false));
        store.setAddress(Utilities.upperCase(store.getAddress(),false));
    }



}
