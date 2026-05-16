package com.magictech.alphapay;
import java.math.BigDecimal;

public class WalletResponse {
    private String id;
    private Long userId;
    private double balance; // Backend ka BigDecimal yahan double mein parse ho jayega
    private String currency;

    // Getters
    public String getId() { return id; }
    public Long getUserId() { return userId; }
    public double getBalance() { return balance; }
    public String getCurrency() { return currency; }
}