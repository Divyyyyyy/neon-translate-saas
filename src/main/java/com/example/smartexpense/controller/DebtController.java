package com.example.smartexpense.controller;

import com.example.smartexpense.dto.DebtView;
import com.example.smartexpense.dto.SettlementView;
import com.example.smartexpense.model.RawDebt;
import com.example.smartexpense.model.Settlement;
import com.example.smartexpense.model.User;
import com.example.smartexpense.service.DebtService;
import com.example.smartexpense.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/debts")
@CrossOrigin
public class DebtController {

    private final DebtService debtService;
    private final UserService userService;

    public DebtController(DebtService debtService, UserService userService) {
        this.debtService = debtService;
        this.userService = userService;
    }

    /**
     * GET /api/debts/raw
     * Returns the "standard" debts: for each edge u -> v, amount.
     */
    @GetMapping("/raw")
    public List<DebtView> getRawDebts() {
        List<RawDebt> rawDebts = debtService.getRawDebts();
        List<DebtView> views = new ArrayList<>();

        for (RawDebt d : rawDebts) {
            Optional<User> fromUserOpt = userService.findById(d.getFromUserId());
            Optional<User> toUserOpt = userService.findById(d.getToUserId());
            if (fromUserOpt.isPresent() && toUserOpt.isPresent()) {
                views.add(new DebtView(
                        fromUserOpt.get().getName(),
                        toUserOpt.get().getName(),
                        d.getAmount()
                ));
            }
        }
        return views;
    }

    /**
     * GET /api/debts/simplified
     * Runs the minimize cash flow algorithm and returns
     * the optimized settlement plan.
     */
    @GetMapping("/simplified")
    public List<SettlementView> getSimplifiedDebts() {
        List<Settlement> settlements = debtService.simplifyDebts();
        List<SettlementView> views = new ArrayList<>();

        for (Settlement s : settlements) {
            Optional<User> fromUserOpt = userService.findById(s.getFromUserId());
            Optional<User> toUserOpt = userService.findById(s.getToUserId());
            if (fromUserOpt.isPresent() && toUserOpt.isPresent()) {
                views.add(new SettlementView(
                        fromUserOpt.get().getName(),
                        toUserOpt.get().getName(),
                        s.getAmount()
                ));
            }
        }
        return views;
    }
}

