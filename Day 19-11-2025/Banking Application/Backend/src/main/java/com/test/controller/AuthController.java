package com.test.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.test.model.Customer;
import com.test.repository.CustomerRepository;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final CustomerRepository repo;

    public AuthController(CustomerRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Customer req) {
        if (req.getUsername() == null || req.getPassword() == null
                || req.getUsername().isBlank() || req.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Username and password required");
        }
        if (repo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        Customer c = new Customer(req.getUsername(), req.getPassword(), 0.0);
        repo.save(c);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer req) {
        Optional<Customer> opt = repo.findByUsername(req.getUsername());
        if (opt.isPresent() && opt.get().getPassword().equals(req.getPassword())) {
            Customer c = opt.get();
            c.setPassword(null); // don't return password
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
