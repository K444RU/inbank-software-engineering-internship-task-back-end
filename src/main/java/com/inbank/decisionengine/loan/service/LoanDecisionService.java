package com.inbank.decisionengine.loan.service;

import com.inbank.decisionengine.loan.dto.LoanDecisionResponse;
import com.inbank.decisionengine.loan.exception.LoanAmountOutOfBoundsException;
import com.inbank.decisionengine.loan.exception.LoanPeriodOutOfBoundsException;
import com.inbank.decisionengine.loan.model.Loan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoanDecisionService {
    private static final int MIN_LOAN_AMOUNT = 2000;
    private static final int MAX_LOAN_AMOUNT = 10000;
    private static final int MAX_LOAN_PERIOD = 60;
    private static final int MIN_LOAN_PERIOD = 12;
    private static final Map<String, Integer> CREDIT_MODIFIERS = new HashMap<>();

    static {
        CREDIT_MODIFIERS.put("49002010965", 0); // debt
        CREDIT_MODIFIERS.put("49002010976", 100); // segment 1
        CREDIT_MODIFIERS.put("49002010987", 300); // segment 2
        CREDIT_MODIFIERS.put("49002010998", 1000); // segment 3
    }

    public LoanDecisionResponse calculateLoanDecision(String personalCode, int loanAmount, int loanPeriod)
            throws LoanAmountOutOfBoundsException, LoanPeriodOutOfBoundsException {
        if (!isAmountInBounds(loanAmount)) {
            throw new LoanAmountOutOfBoundsException();
        } else if (!isPeriodInBounds(loanPeriod)) {
            throw new LoanPeriodOutOfBoundsException();
        }

        Loan loan = new Loan(personalCode, loanAmount, loanPeriod);

        Integer creditModifier = CREDIT_MODIFIERS.get(personalCode);
        if (creditModifier == null) {
            return new LoanDecisionResponse(false, 0, 0, "Negative");
        }

        if (loan.getCreditModifier() == 0) {
            return new LoanDecisionResponse(false, 0, 0, "Negative, person has debt");
        } else {
            double creditScore = calculateCreditScore(loan.getCreditModifier(), loan.getLoanAmount(), loan.getLoanPeriod());
            loan.setCreditScore(creditScore);

            adjustLoanAmount(loan);

            if (loan.getApprovedLoanAmount() < MIN_LOAN_AMOUNT) {
                findSuitableLoanPeriod(loan);
            }

            if (loan.getApprovedLoanAmount() < MIN_LOAN_AMOUNT) {
                return new LoanDecisionResponse(false, 0, 0, "Negative");
            } else {
                return new LoanDecisionResponse(true, loan.getApprovedLoanAmount(), loan.getLoanPeriod(), "Positive");
            }
        }
    }

    private void adjustLoanAmount(Loan loan) {
        int adjustedLoanAmount = loan.getLoanAmount();
        double adjustedCreditScore = loan.getCreditScore();
        if (loan.getCreditScore() > 1.0) {
            while (adjustedCreditScore > 1.0 && adjustedLoanAmount < MAX_LOAN_AMOUNT) {
                adjustedLoanAmount += 100;
                adjustedCreditScore = calculateCreditScore(loan.getCreditModifier(), adjustedLoanAmount, loan.getLoanPeriod());
            }
        } else if (loan.getCreditScore() < 1.0) {
            while (adjustedCreditScore < 1.0 && adjustedLoanAmount > MIN_LOAN_AMOUNT) {
                adjustedLoanAmount -= 100;
                adjustedCreditScore = calculateCreditScore(loan.getCreditModifier(), adjustedLoanAmount, loan.getLoanPeriod());
            }
        }
        loan.setLoanAmount(adjustedLoanAmount);
        loan.setCreditScore(adjustedCreditScore);
        int approvedLoanAmount = (int) (Math.min(adjustedCreditScore, 1.0) * adjustedLoanAmount);
        loan.setApprovedLoanAmount(approvedLoanAmount);
    }

    private void findSuitableLoanPeriod(Loan loan) {
        for (int i = loan.getLoanPeriod(); i < MAX_LOAN_PERIOD; i++) {
            double creditScore = calculateCreditScore(loan.getCreditModifier(), loan.getLoanAmount(), i);
            if (creditScore >= 1) {
                loan.setLoanPeriod(i);
                loan.setApprovedLoanAmount(loan.getLoanAmount());
                break;
            }
        }
    }

    private boolean isPeriodInBounds(int loanPeriod) {
        return loanPeriod >= MIN_LOAN_PERIOD && loanPeriod <= MAX_LOAN_PERIOD;
    }

    private boolean isAmountInBounds(int loanAmount) {
        return loanAmount >= MIN_LOAN_AMOUNT && loanAmount <= MAX_LOAN_AMOUNT;
    }

    private double calculateCreditScore(int creditModifier, int loanAmount, int loanPeriod) {
        return (double) creditModifier / loanAmount * loanPeriod;
    }

}
