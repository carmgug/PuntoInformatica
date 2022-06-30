package it.carmelogug.puntoinformatica.entities.purchasing;


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


    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE,orphanRemoval = true)
    private List<StoredProductInCart> storedProductsInCart;

}
