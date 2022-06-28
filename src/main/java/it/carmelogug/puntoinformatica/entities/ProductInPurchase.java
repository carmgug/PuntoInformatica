package it.carmelogug.puntoinformatica.entities;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
@Entity
@Getter
@Setter
@Table(name = "ProductsInPurchase", schema = "punto_informatica")
public class ProductInPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "quantity", nullable = true)
    private int quantity;

    @Basic
    @Column(name = "price",nullable = true)
    private double price;

    @ManyToOne
    @JoinColumn(name = "related_purchase")
    @JsonIgnore
    @ToString.Exclude
    private Purchase purchase;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "stored_product")
    private StoredProduct storedProduct;
}
