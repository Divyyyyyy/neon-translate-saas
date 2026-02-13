package com.example.smartexpense.model;

/**
 * Represents a simplified settlement "transaction":
 * fromUser should pay toUser the given amount after minimization.
 */
public class Settlement {
    private Long fromUserId;  // debtor
    private Long toUserId;    // creditor
    private double amount;

    public Settlement() {
    }

    public Settlement(Long fromUserId, Long toUserId, double amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public double getAmount() {
        return amount;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

