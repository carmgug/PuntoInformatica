package it.carmelogug.puntoinformatica.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.carmelogug.puntoinformatica.entities.purchasing.Cart;
import it.carmelogug.puntoinformatica.entities.purchasing.Purchase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name= "Users",schema = "punto_informatica")
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false)
    private int id;

    /*
        Campi obblicatori
     */
    @NotBlank(message = "First name may not be blank!")
    @Basic
    @Column(name="first_name",nullable = false)
    private String firstName;

    @NotBlank(message = "Last name may not be blank!")
    @Basic
    @Column(name="last_name",nullable = false)
    private String lastName;

    //un utente è identificato dall'email
    @Basic
    @NotBlank(message = "Email may not be blank!")
    @Column(name="email",nullable = false,unique = true)
    private String email;

    @Basic
    @Column(name="phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.MERGE,orphanRemoval = true)
    @JsonIgnore
    private List<Purchase> purchases;

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.REMOVE,orphanRemoval = true)
    @JsonIgnore
    private Cart cart;

    //costruttori
    public User(String email,String firstName,String lastName,String phoneNumber){
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phoneNumber=phoneNumber;
    }
    public User(){}




}
