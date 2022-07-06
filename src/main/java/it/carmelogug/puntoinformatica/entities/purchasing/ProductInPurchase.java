package it.carmelogug.puntoinformatica.entities.purchasing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.Store;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
@Entity
@Getter
@Setter
@Table(name = "products_in_purchase", schema = "punto_informatica")
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



    /*

     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "store")
    private Store store;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product")
    private Product product;
}
