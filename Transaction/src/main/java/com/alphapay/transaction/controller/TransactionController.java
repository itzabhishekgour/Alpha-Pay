package com.alphapay.transaction.controller;

import com.alphapay.transaction.dto.TransactionRequest;
import com.alphapay.transaction.entity.Transaction;
import com.alphapay.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alphapay/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/record")
    public ResponseEntity<Transaction> recordTransaction(@RequestBody TransactionRequest request) {
        Transaction savedTxn = transactionService.recordTransaction(request);
        return ResponseEntity.ok(savedTxn);
    }


    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Long userId) {
        List<Transaction> history = transactionService.getTransactionHistory(userId);
        return ResponseEntity.ok(history);
    }
}