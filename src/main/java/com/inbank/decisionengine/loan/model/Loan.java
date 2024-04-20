package com.inbank.decisionengine.loan.model;

import lombok.Data;

@Data
public class Loan {
    private String personalCode;
    private int loanAmount;
    private int loanPeriod;
    private int creditModifier;
    private double creditScore;
    private int approvedLoanAmount;

    public Loan(String personalCode, int approvedLoanAmount, int loanPeriod){
        this.personalCode = personalCode;
        this.approvedLoanAmount = approvedLoanAmount;
        this.loanPeriod = loanPeriod;
    }
}
