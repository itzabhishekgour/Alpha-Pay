package com.alphapay.wallet.service;

import com.alphapay.wallet.dto.TransactionRequest;
import com.alphapay.wallet.entity.Wallet;
import com.alphapay.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private RestTemplate restTemplate;

//    private final String TRANSACTION_SERVICE_URL = "http://localhost:8082/alphapay/transactions/record";
private final String TRANSACTION_SERVICE_URL = "http://transaction-service:8082/alphapay/transactions/record";

    public Wallet createWalletForNewUser(Long userId) {
        //making wallet an object
        Wallet newWallet = new Wallet();

        //set value in wallet
        newWallet.setId("W" + userId);
        newWallet.setUserId(userId);
        newWallet.setBalance(BigDecimal.ZERO);
        newWallet.setCurrency("INR");

        return walletRepository.save(newWallet);
    }

    @Transactional
    public Wallet addMoney(Long userId, BigDecimal amount) {
        // 1. Fetch the user's wallet
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));

        // 2. Update the balance
        wallet.setBalance(wallet.getBalance().add(amount));
        Wallet updatedWallet = walletRepository.save(wallet);

        // 3. Communicate with Transaction Service
        try {
            TransactionRequest txnRequest = new TransactionRequest(
                    "EXTERNAL_BANK", // Kyunki user bahar se paise add kar raha hai
                    userId,
                    amount,
                    "SUCCESS"
            );

            ResponseEntity<String> response = restTemplate.postForEntity(
                    TRANSACTION_SERVICE_URL,
                    txnRequest,
                    String.class
            );

            System.out.println("Transaction Service Response: " + response.getStatusCode());

        } catch (Exception e) {

            throw new RuntimeException("Failed to record transaction. Rolling back add money operation. Error: " + e.getMessage());
        }

        return updatedWallet;
    }
}
