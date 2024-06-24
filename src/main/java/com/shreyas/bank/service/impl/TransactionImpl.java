/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.shreyas.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shreyas.bank.dto.TransactionDto;
import com.shreyas.bank.entity.Transaction;
import com.shreyas.bank.repository.TransactionRepository;


/**
 *
 * @author Shreyas
 */
@Component
public class TransactionImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction= Transaction.builder()
            .transactionType(transactionDto.getTransactionType())
            .accountNumber(transactionDto.getAccountNumber())
            .amount(transactionDto.getAmount())
            .status("SUCCESS")
            .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }

}
