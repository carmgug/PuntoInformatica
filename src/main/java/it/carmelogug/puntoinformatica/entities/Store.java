package it.carmelogug.puntoinformatica.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table( name="Stores", schema="punto_informatica")
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
    @NotBlank(message = "City may not be blank")
    @Column(name="city",length = 50)
    private String city;

    @Basic
    @NotBlank(message = "Region may not be blank")

    @Column(name="region",length = 50)
    private String region;

    @Basic
    @NotBlank(message = "Country may not be blank")
    @Column(name="country",length = 50)
    private String country;

    @Basic
    @NotBlank(message = "Province may not be blank")
    @Column(name="province",length = 5)
    private String province;

    @Basic
    @NotBlank(message = "Address may not be blank")
    @Column(name="address")
    private String address;

    @Basic
    @Column(name="postal_code",nullable = true)
    private Long postalCode;



    /*
        INFORMATION REGARDING THE STORED PRODUCTS.
     */

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "store",cascade = CascadeType.MERGE,orphanRemoval = true)
    private List<StoredProduct> storedProducts;

}
