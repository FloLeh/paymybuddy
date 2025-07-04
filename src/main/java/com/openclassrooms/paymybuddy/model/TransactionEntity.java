package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    private BigDecimal amount = BigDecimal.valueOf(0.);

    public TransactionEntity(UserEntity sender, UserEntity receiver, String description, BigDecimal amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.description = description;
        this.amount = amount;
    }

}
