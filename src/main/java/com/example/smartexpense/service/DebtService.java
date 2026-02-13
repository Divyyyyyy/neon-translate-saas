package com.example.smartexpense.service;

import com.example.smartexpense.dto.AddExpenseRequest;
import com.example.smartexpense.model.RawDebt;
import com.example.smartexpense.model.Settlement;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Core DSA service:
 *
 * - Represents users as nodes in a graph.
 * - Raw debts are stored as directed edges (from -> to, amount).
 * - We maintain a "net balance" for each user:
 *     > 0  => this user should receive money (creditor)
 *     < 0  => this user owes money (debtor)
 *
 * - The "Minimize Cash Flow" algorithm:
 *   1. Compute net amount for each user using all raw debts.
 *   2. Split users into:
 *        - creditors (positive net)
 *        - debtors (negative net)
 *   3. Use a greedy approach with two priority queues:
 *        - maxCreditPQ: user with the largest positive net at the top
 *        - maxDebitPQ:  user with the largest *absolute* negative net at the top
 *   4. While both queues are non-empty:
 *        - Pick top creditor C and top debtor D.
 *        - Settle min(C.credit, |D.debt|) amount:
 *            D pays C this amount.
 *        - Decrease/update their net amounts.
 *        - Reinsert them into the queues if they still have non-zero net.
 *
 * - This approach typically reduces from O(N^2) naive pairwise settlements
 *   to O(N) settlements in practice because each user appears in
 *   at most a few settlements.
 */
@Service
public class DebtService {

    private final UserService userService;

    /**
     * Raw list of standard debts (edges in the graph).
     * Each expense is broken into multiple RawDebt entries.
     */
    private final List<RawDebt> rawDebts = new ArrayList<>();

    /**
     * Current net balance for each user.
     * - Positive: user should receive money (creditor).
     * - Negative: user owes money (debtor).
     *
     * Example:
     *   netBalance.get(userId) = +10.0  => user is owed 10
     *   netBalance.get(userId) = -5.0   => user owes 5
     */
    private final Map<Long, Double> netBalance = new HashMap<>();

    public DebtService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Add an expense to the system.
     *
     * Example: "Alice paid 30 for Lunch, split between Alice, Bob, Charlie"
     *
     * Let participants = [Alice, Bob, Charlie], amount = 30.
     * Each person should ultimately pay 10.
     *   - If Alice paid 30:
     *       - Bob owes Alice 10 (RawDebt: Bob -> Alice, 10)
     *       - Charlie owes Alice 10 (RawDebt: Charlie -> Alice, 10)
     *       - For Alice herself, she paid 30 but only owes 10,
     *         so her *net* contribution is +20.
     *
     * We update:
     *   - rawDebts list (standard debts)
     *   - netBalance map (net per user)
     */
    public void addExpense(AddExpenseRequest request) {
        Long payerId = request.getPaidByUserId();
        List<Long> participants = request.getParticipantUserIds();
        double amount = request.getAmount();

        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Participants list cannot be empty");
        }

        int n = participants.size();
        double share = amount / n; // each participant's fair share

        for (Long participantId : participants) {
            if (userService.findById(participantId).isEmpty()) {
                throw new IllegalArgumentException("Unknown user id in participants: " + participantId);
            }
        }

        if (userService.findById(payerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown payer user id: " + payerId);
        }

        for (Long participantId : participants) {
            if (!participantId.equals(payerId)) {
                // participant owes 'share' to payer
                rawDebts.add(new RawDebt(participantId, payerId, share));

                // Update net balances:
                // - Debtor (participant) decreases by 'share'
                // - Creditor (payer) increases by 'share'
                netBalance.put(participantId, netBalance.getOrDefault(participantId, 0.0) - share);
                netBalance.put(payerId, netBalance.getOrDefault(payerId, 0.0) + share);
            } else {
                // For payer's own share we don't create a RawDebt; net is handled via others' debts.
                netBalance.putIfAbsent(payerId, netBalance.getOrDefault(payerId, 0.0));
            }
        }
    }

    /**
     * Returns the current list of standard/raw debts
     * (who owes whom directly, before simplification).
     */
    public List<RawDebt> getRawDebts() {
        return new ArrayList<>(rawDebts);
    }

    /**
     * Returns a copy of the current net balances for each user.
     */
    public Map<Long, Double> getNetBalances() {
        return new HashMap<>(netBalance);
    }

    /**
     * Core "Minimize Cash Flow" algorithm.
     */
    public List<Settlement> simplifyDebts() {
        // Helper class to hold userId + amount
        class BalanceNode {
            Long userId;
            double amount;

            BalanceNode(Long userId, double amount) {
                this.userId = userId;
                this.amount = amount;
            }
        }

        // Max-heap for creditors (largest positive first)
        PriorityQueue<BalanceNode> maxCreditPQ = new PriorityQueue<>(
                (a, b) -> Double.compare(b.amount, a.amount)
        );

        // Max-heap for debtors by absolute value (most negative first)
        PriorityQueue<BalanceNode> maxDebitPQ = new PriorityQueue<>(
                (a, b) -> Double.compare(Math.abs(b.amount), Math.abs(a.amount))
        );

        // Populate the queues from the netBalance map
        for (Map.Entry<Long, Double> entry : netBalance.entrySet()) {
            Long userId = entry.getKey();
            double amount = entry.getValue();

            if (Math.abs(amount) < 1e-6) {
                continue; // treat very small amounts as zero
            }

            if (amount > 0) {
                maxCreditPQ.add(new BalanceNode(userId, amount));
            } else if (amount < 0) {
                maxDebitPQ.add(new BalanceNode(userId, amount)); // negative
            }
        }

        List<Settlement> result = new ArrayList<>();

        // Greedy pairing: largest creditor with largest debtor
        while (!maxCreditPQ.isEmpty() && !maxDebitPQ.isEmpty()) {
            BalanceNode creditor = maxCreditPQ.poll(); // largest positive
            BalanceNode debtor = maxDebitPQ.poll();    // most negative

            double credit = creditor.amount;
            double debt = -debtor.amount; // positive magnitude

            // Amount to be settled in this step
            double settledAmount = Math.min(credit, debt);

            if (settledAmount > 0) {
                // Debtor pays creditor settledAmount
                result.add(new Settlement(debtor.userId, creditor.userId, settledAmount));

                // Update their net amounts
                creditor.amount -= settledAmount; // reduced by what they received
                debtor.amount += settledAmount;   // moves towards zero (less negative)
            }

            // Reinsert into PQs if there is remaining balance for either side
            if (creditor.amount > 1e-6) {
                maxCreditPQ.add(creditor);
            }
            if (debtor.amount < -1e-6) {
                maxDebitPQ.add(debtor);
            }
        }

        return result;
    }

    /**
     * Utility to reset everything (for demo/testing).
     */
    public void clearAll() {
        rawDebts.clear();
        netBalance.clear();
    }
}

