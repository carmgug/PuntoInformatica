package it.carmelogug.puntoinformatica.entities.store;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.carmelogug.puntoinformatica.entities.purchasing.StoredProductInCart;
import it.carmelogug.puntoinformatica.entities.store.Product;
import it.carmelogug.puntoinformatica.entities.store.Store;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


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
    private Integer id;

    @Version
    @Column(name = "version", nullable = false)
    @JsonIgnore
    private long version;

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

    @OneToMany(mappedBy = "storedProduct", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<StoredProductInCart> storedProductsInCart;





}
