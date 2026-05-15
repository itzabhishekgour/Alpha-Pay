package com.alphapay.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "alphapay_wallet_db")
public class Wallet {
    @Id
    private String id; // Example: "W1"

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false, length = 3)
    private String currency = "INR";
public Wallet(){}
    public Wallet(String walletId, Long userId, BigDecimal zero, String inr) {
    }
}
