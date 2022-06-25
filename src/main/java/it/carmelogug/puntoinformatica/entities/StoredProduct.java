package it.carmelogug.puntoinformatica.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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
    @Column(name ="quantity",nullable = false)
    private int quantity;

    @Basic
    @Column(name="price",nullable = false)
    private double price;


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
