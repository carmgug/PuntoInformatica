package it.carmelogug.puntoinformatica.support;

import it.carmelogug.puntoinformatica.entities.Product;

public final class Utilities {

        /*
        Permette di restituire l'upperCase di una stringa, gestendo il caso anche in cui essa sia nulla.
         */
        public static String upperCase(String str) {
            if (str == null) {
                return null;
            }
            return str.toUpperCase();
        }

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
        }

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
    }



}
