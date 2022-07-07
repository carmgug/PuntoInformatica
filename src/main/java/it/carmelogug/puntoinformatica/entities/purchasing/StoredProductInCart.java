package it.carmelogug.puntoinformatica.entities.purchasing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.carmelogug.puntoinformatica.entities.store.StoredProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "stored_products_in_cart", schema = "punto_informatica")
public class StoredProductInCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private int id;

    @Basic
    @Column(name = "quantity",nullable = true)
    private int quantity;

    @ManyToOne
    @JoinColumn(name= "related_cart")
    @JsonIgnore
    @ToString.Exclude
    private Cart cart;

    @Version
    @Column(name = "version", nullable = false)
    @JsonIgnore
    private long version;

    @ManyToOne
    @JoinColumn(name="stored_product")
    private StoredProduct storedProduct;




}
