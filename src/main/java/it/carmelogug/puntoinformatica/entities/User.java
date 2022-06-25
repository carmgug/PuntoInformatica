package it.carmelogug.puntoinformatica.entities;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @NotBlank(message = "Email may not be blank!")
    @Basic
    @Column(name="email",nullable = true)
    private String email;

    @Basic
    @Column(name="phone_number")
    private String phoneNumber;



}
