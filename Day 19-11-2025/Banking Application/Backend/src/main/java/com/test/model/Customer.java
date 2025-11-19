package com.test.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // default value to avoid nulls
    @Column(nullable = false)
    private Double balance = 0.0;

    public Customer() {}

    public Customer(String username, String password, Double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance == null ? 0.0 : balance;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}
