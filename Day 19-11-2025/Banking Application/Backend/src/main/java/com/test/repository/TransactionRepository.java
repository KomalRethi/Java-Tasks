package com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.test.model.Transaction;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUsernameOrderByTimestampDesc(String username);
}
