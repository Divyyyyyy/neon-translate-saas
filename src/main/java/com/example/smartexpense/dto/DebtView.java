package com.example.smartexpense.dto;

/**
 * View object to send to frontend for standard/raw debts:
 * clearly states "Alice owes Bob X".
 */
public class DebtView {
    private String fromUserName;
    private String toUserName;
    private double amount;

    public DebtView() {
    }

    public DebtView(String fromUserName, String toUserName, double amount) {
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.amount = amount;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public double getAmount() {
        return amount;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

