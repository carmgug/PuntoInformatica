package it.carmelogug.puntoinformatica.entities.store;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table( name="stores", schema="punto_informatica")
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="store_id",nullable = false)
    private Integer id;


    /*
        INFORMATION REGARDING THE LOCATION OF THE STORE.
     */


    @Basic
    @Column(name="country",length = 50)
    private String country;

    @Basic
    @Column(name="city",length = 50)
    private String city;

    @Basic
    @Column(name="region",length = 50)
    private String region;

    @Basic
    @Column(name="province",length = 5)
    private String province;

    @Basic
    @Column(name="address")
    private String address;

    @Basic
    private Long postalCode;

    @Basic
    @Column(name="is_banned", columnDefinition = "boolean default false")
    private boolean banned;



    /*
        INFORMATION REGARDING THE STORED PRODUCTS.
     */

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "store",cascade = {CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<StoredProduct> storedProducts;

}
