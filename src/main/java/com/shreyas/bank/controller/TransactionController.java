package com.shreyas.bank.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.shreyas.bank.entity.Transaction;
import com.shreyas.bank.service.impl.BankStatement;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {

    private final BankStatement bankStatement;

    @GetMapping("/generateStatement")
    public List<Transaction> generateStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws FileNotFoundException, DocumentException{
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }

}   
