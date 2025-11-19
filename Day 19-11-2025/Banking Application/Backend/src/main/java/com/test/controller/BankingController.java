package com.test.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.test.repository.CustomerRepository;
import com.test.repository.TransactionRepository;
import com.test.model.Customer;
import com.test.model.Transaction;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/bank")
public class BankingController {

    private final CustomerRepository repo;
    private final TransactionRepository tRepo;

    public BankingController(CustomerRepository repo, TransactionRepository tRepo) {
        this.repo = repo;
        this.tRepo = tRepo;
    }

    @GetMapping("/balance/{username}")
    public ResponseEntity<?> getBalance(@PathVariable String username) {
        Optional<Customer> opt = repo.findByUsername(username);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        Customer c = opt.get();
        if (c.getBalance() == null) {
            c.setBalance(0.0);
            repo.save(c);
        }
        // save a balance check transaction
        tRepo.save(new Transaction(username, "BalanceCheck", c.getBalance()));
        return ResponseEntity.ok(c.getBalance());
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam String username, @RequestParam Double amount) {
        if (amount == null || amount <= 0) return ResponseEntity.badRequest().body("Amount must be > 0");
        Optional<Customer> opt = repo.findByUsername(username);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        Customer c = opt.get();
        c.setBalance(c.getBalance() + amount);
        repo.save(c);
        tRepo.save(new Transaction(username, "Deposit", amount));
        return ResponseEntity.ok("Deposit successful. New balance: " + c.getBalance());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam String username, @RequestParam Double amount) {
        if (amount == null || amount <= 0) return ResponseEntity.badRequest().body("Amount must be > 0");
        Optional<Customer> opt = repo.findByUsername(username);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        Customer c = opt.get();
        if (c.getBalance() < amount) return ResponseEntity.badRequest().body("Insufficient balance");
        c.setBalance(c.getBalance() - amount);
        repo.save(c);
        tRepo.save(new Transaction(username, "Withdraw", amount));
        return ResponseEntity.ok("Withdraw successful. New balance: " + c.getBalance());
    }
}
