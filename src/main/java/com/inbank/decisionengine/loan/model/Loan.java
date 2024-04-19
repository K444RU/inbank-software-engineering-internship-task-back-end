package com.inbank.decisionengine.loan.model;

import lombok.Data;

@Data
public class Loan {
    private String personalCode;
    private int loanAmount;
    private int loanPeriod;
    private int creditModifier;
    private double creditScore;
    private int approvedAmount;

    public Loan(String personalCode, int loanAmount, int loanPeriod){
        this.personalCode = personalCode;
        this.loanAmount = loanAmount;
        this.loanPeriod = loanPeriod;
    }
}
