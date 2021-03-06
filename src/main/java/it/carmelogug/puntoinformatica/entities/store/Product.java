package it.carmelogug.puntoinformatica.entities.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "Products", schema= "punto_informatica")
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer id;


    @Basic
    @NotBlank(message = "name may not be blank!")
    @Column(name = "product_name",nullable = false)
    private String name;

    @Basic
    @NotNull(message = "type may not be null!")
    @Enumerated(EnumType.STRING)
    @Column(name="type",nullable = false)
    private Type type;

    @Basic
    @NotNull(message = "category may not be null!")
    @Enumerated(EnumType.STRING)
    @Column(name = "category",nullable = false)
    private Category category;

    @Basic
    @NotNull(message = "barCode may not be null!")
    @Column(name="bar_code",nullable = false)
    private Long barCode;

    @Basic
    @Column(name="is_banned", columnDefinition = "boolean default false")
    private boolean banned;

    @Basic
    @Column(name= "description",nullable = true,length = 500)
    @ToString.Exclude
    private String description;

    @Basic
    @Column(name= "url_product",nullable = true,length = 500)
    @ToString.Exclude
    private String url; //Sito del produttore

    @Basic
    @Column(name="url_image",nullable = true,length = 500)
    @ToString.Exclude
    private String urlImage;

    /*
        Campi riguardanti il mapping
     */
    @OneToMany(mappedBy = "product",cascade = {CascadeType.MERGE,CascadeType.REMOVE}, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<StoredProduct> storedProducts;


    public enum Type{
        SOFTWARE,HARDWARE
    }

    public enum Category{
        //HARDWARE
        ACCESSORIESandPERIPHERALS,COMPONENT,COMPUTER,MAC,MONITORS,SMARTPHONE,
        //SOFTWARE
        VIDEOGAMES,SYSTEMSOFTWARE,APPLICATIONSOFTWARE
    }




}
