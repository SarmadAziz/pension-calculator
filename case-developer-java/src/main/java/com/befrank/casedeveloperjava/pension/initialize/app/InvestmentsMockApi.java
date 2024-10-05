package com.befrank.casedeveloperjava.pension.initialize.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pensionfund")
public class InvestmentsMockApi {

    @GetMapping(value = "/{accountNumber}", produces = "application/json")
    public String getAccount() {
        return "{ \"investments\": [{ \"investment\": \"investment1\", \"amount\": 60000.00 }, { \"investment\": \"investment2\", \"amount\": 40000.00 }] }";
    }
}
