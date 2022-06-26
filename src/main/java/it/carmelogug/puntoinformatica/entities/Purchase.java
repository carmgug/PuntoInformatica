package it.carmelogug.puntoinformatica.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
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

    @ManyToOne
    @JoinColumn(name = "buyer")
    private User buyer;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.MERGE)
    private List<ProductInPurchase> productsInPurchase;
}
