package com.inbank.decisionengine.loan.controller;

import com.inbank.decisionengine.loan.dto.LoanDecisionResponse;
import com.inbank.decisionengine.loan.service.LoanDecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanDecisionController {

    private final LoanDecisionService loanDecisionService;

    @Autowired
    public LoanDecisionController(LoanDecisionService loanDecisionService) {
        this.loanDecisionService = loanDecisionService;
    }
    @GetMapping("/decision")
    public ResponseEntity<?> getLoanDecision(@RequestParam String personalCode,
                                             @RequestParam int loanAmount,
                                             @RequestParam int loanPeriod){
        LoanDecisionResponse loanDecisionResponse = loanDecisionService.calculateLoanDecision(personalCode, loanAmount, loanPeriod);
        return new ResponseEntity<>(loanDecisionResponse, HttpStatus.OK);
    }

}
