package com.alphapay.wallet.controller;

import com.alphapay.wallet.entity.Wallet;
import com.alphapay.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/alphapay/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<String> createWallet(@RequestParam Long userId) {
        walletService.createWalletForNewUser(userId);
        return ResponseEntity.ok("Wallet successfully created for user: " + userId);
    }

    @PostMapping("/{userId}/add-money")
    public ResponseEntity<String> addMoney(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero.");
        }

        Wallet updatedWallet = walletService.addMoney(userId, amount);

        return ResponseEntity
                .ok("Successfully added " + amount + " to wallet. New Balance: " + updatedWallet.getBalance());
    }

    @GetMapping("/{userId}/balance") // Path variable format
    public ResponseEntity<Wallet> getBalance(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }
}
