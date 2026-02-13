package com.example.smartexpense.model;

/**
 * Represents a direct "standard" debt edge in the graph:
 * fromUser owes toUser a given amount.
 *
 * This is before any simplification / graph optimization.
 */
public class RawDebt {
    private Long fromUserId;  // debtor
    private Long toUserId;    // creditor
    private double amount;

    public RawDebt() {
    }

    public RawDebt(Long fromUserId, Long toUserId, double amount) {
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

