package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private UserEntity sender;

    @ManyToOne
    @JoinColumn
    private UserEntity receiver;

    private String description;

    private Double amount = 0.;

}
