package com.test.controller;

import org.springframework.web.bind.annotation.*;
import com.test.model.Transaction;
import com.test.repository.TransactionRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository repo;

    public TransactionController(TransactionRepository repo) {
        this.repo = repo;
    }

    // get transactions for the specific username (most recent first)
    @GetMapping("/{username}")
    public List<Transaction> getUserTransactions(@PathVariable String username) {
        return repo.findByUsernameOrderByTimestampDesc(username);
    }
}
