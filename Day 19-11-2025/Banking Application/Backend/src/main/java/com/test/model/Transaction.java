package com.test.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // store username string for simplicity and easy queries
    private String username;

    private String type;   // "Deposit", "Withdraw", "BalanceCheck"
    private Double amount; // may be 0 for BalanceCheck

    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(String username, String type, Double amount) {
        this.username = username;
        this.type = type;
        this.amount = amount == null ? 0.0 : amount;
        this.timestamp = LocalDateTime.now();
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
