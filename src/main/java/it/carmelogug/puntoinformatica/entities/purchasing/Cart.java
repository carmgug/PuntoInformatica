package it.carmelogug.puntoinformatica.entities.purchasing;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.carmelogug.puntoinformatica.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="cart",schema = "punto_informatica")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;


    @OneToOne
    @JoinColumn(name="buyer")
    private User buyer;

    @Version
    @Column(name = "version", nullable = false)
    @JsonIgnore
    private long version;

    @OneToMany(mappedBy = "cart",cascade = {CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<StoredProductInCart> storedProductsInCart;

}
