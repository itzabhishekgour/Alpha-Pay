package com.alphapay.transaction.service;

import com.alphapay.transaction.dto.TransactionRequest;
import com.alphapay.transaction.entity.Transaction;
import com.alphapay.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction recordTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction(
                request.getSenderId(),
                request.getReceiverId(),
                request.getAmount(),
                request.getStatus()
        );
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findBySenderIdOrReceiverIdOrderByTimestampDesc(
                String.valueOf(userId), userId);
    }
}