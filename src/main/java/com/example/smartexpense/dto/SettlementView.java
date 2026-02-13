package com.example.smartexpense.dto;

public class SettlementView {
    private String fromUserName;
    private String toUserName;
    private double amount;

    public SettlementView() {
    }

    public SettlementView(String fromUserName, String toUserName, double amount) {
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

