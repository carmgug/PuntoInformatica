package it.carmelogug.puntoinformatica.entities.purchasing;

import it.carmelogug.puntoinformatica.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Getter
@Setter
@Table(name = "Purchases", schema = "punto_informatica")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    private Date purchaseTime;

    @Basic
    @Column(name= "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "buyer")
    private User buyer;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.MERGE)
    private List<StoredProductInPurchase> productsInPurchase;
}
