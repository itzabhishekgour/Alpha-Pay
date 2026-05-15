package com.alphapay.transaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private String senderId;
    private Long receiverId;
    private BigDecimal amount;
    private String status;

}