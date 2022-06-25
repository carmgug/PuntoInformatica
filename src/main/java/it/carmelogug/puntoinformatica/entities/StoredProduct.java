package it.carmelogug.puntoinformatica.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "stored_products", schema = "punto_informatica")
public class StoredProduct {

    /*
        Campi obbligatori
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;



    @Basic
    @PositiveOrZero(message = "Quantity must be positive or zero!")
    @NotNull(message = "Quantity may not be null!")
    @Column(name ="quantity",nullable = false)
    private Integer quantity;

    @Basic
    @Positive(message = "Price must be positive!")
    @NotNull(message = "price may not be null!")
    @Column(name="price",nullable = false)
    private Double price;


    /*
        INFORMATION REGARDING STORE AND PRODUCT
     */

    @ManyToOne
    @JoinColumn(name = "related_store")
    @ToString.Exclude
    private Store store;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name="product_id")
    private Product product;






}
