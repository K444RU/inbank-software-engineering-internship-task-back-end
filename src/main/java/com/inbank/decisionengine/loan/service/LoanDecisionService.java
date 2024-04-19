package com.inbank.decisionengine.loan.service;

import com.inbank.decisionengine.loan.dto.LoanDecisionResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoanDecisionService {
    private static final int MIN_LOAN_AMOUNT = 2000;
    private static final int MAX_LOAN_AMOUNT = 10000;
    private static final int MIN_LOAN_PERIOD = 12;
    private static final int MAX_LOAN_PERIOD = 60;
    private static final Map<String, Integer> CREDIT_MODIFIERS = new HashMap<>();


    static {
        CREDIT_MODIFIERS.put("49002010965", 0); // debt
        CREDIT_MODIFIERS.put("49002010976", 100); // segment 1
        CREDIT_MODIFIERS.put("49002010987", 300); // segment 2
        CREDIT_MODIFIERS.put("49002010998", 1000); // segment 3
    }

    public LoanDecisionResponse calculate(String personalCode, int loanAmount, int loanPeriod) {
        return null;
    }
}
