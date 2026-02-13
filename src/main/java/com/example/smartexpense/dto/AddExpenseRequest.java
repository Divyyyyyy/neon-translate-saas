package com.example.smartexpense.dto;

import java.util.List;

/**
 * Example JSON:
 * {
 *   "paidByUserId": 1,
 *   "participantUserIds": [1, 2, 3],
 *   "amount": 30.0,
 *   "description": "Lunch"
 * }
 */
public class AddExpenseRequest {

    private Long paidByUserId;
    private List<Long> participantUserIds;
    private double amount;
    private String description;

    public AddExpenseRequest() {
    }

    public Long getPaidByUserId() {
        return paidByUserId;
    }

    public void setPaidByUserId(Long paidByUserId) {
        this.paidByUserId = paidByUserId;
    }

    public List<Long> getParticipantUserIds() {
        return participantUserIds;
    }

    public void setParticipantUserIds(List<Long> participantUserIds) {
        this.participantUserIds = participantUserIds;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

