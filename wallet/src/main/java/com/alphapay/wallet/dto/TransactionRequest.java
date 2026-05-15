package com.alphapay.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private String senderId;
    private Long receiverId;
    private BigDecimal amount;
    private String status;


    public TransactionRequest() {}

    public TransactionRequest(String senderId, Long receiverId, BigDecimal amount, String status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }


    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}