package com.inbank.decisionengine.loan.dto;

import lombok.Data;

@Data
public class LoanDecisionResponse {
    private boolean isApproved;
    private int approvedAmount;
    private int approvedPeriod;
    private String decisionDescription;

    public LoanDecisionResponse(boolean isApproved, int approvedAmount, int approvedPeriod, String decisionDescription) {
        this.isApproved = isApproved;
        this.approvedAmount = approvedAmount;
        this.approvedPeriod = approvedPeriod;
        this.decisionDescription = decisionDescription;
    }
}
