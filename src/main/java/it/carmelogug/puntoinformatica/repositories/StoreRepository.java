package it.carmelogug.puntoinformatica.repositories;

import it.carmelogug.puntoinformatica.entities.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store,Integer> {


    Store findStoreById(int storeID);

    Store findByCountryAndRegionAndCityAndProvinceAndAddress(
            String country,String region,
            String city,String province,String address);

    @Query("SELECT s " +
            "FROM Store as s " +
            "WHERE (upper(s.country) LIKE :country OR :country IS NULL ) AND " +
            "      (upper(s.region) LIKE :region OR :region IS NULL ) AND " +
            "       (upper(s.city) LIKE :city OR :city IS NULL ) AND " +
            "       (upper(s.province) LIKE :province OR :province IS NULL ) AND " +
            "       (upper(s.address) LIKE :address OR :address IS NULL )")
    List<Store> advSearchByCountryAndRegionAndCityAndProvinceAndAddress(
            String country, String region,
            String city, String province, String address);

    boolean existsByCountryAndRegionAndCityAndProvinceAndAddress(
            String country, String region, String city, String province, String address);
}
