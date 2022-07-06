package it.carmelogug.puntoinformatica.support;

import it.carmelogug.puntoinformatica.entities.User;
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
