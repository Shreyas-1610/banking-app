/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.shreyas.bank.service.impl;

import com.shreyas.bank.dto.BankResponse;
import com.shreyas.bank.dto.CreditDebitRequest;
import com.shreyas.bank.dto.EnquiryRequest;
import com.shreyas.bank.dto.LoginDto;
import com.shreyas.bank.dto.TransferRequest;
import com.shreyas.bank.dto.UserRequest;

/**
 *
 * @author Shreyas
 */
public interface UserService {
   BankResponse createAccount(UserRequest userRequest);


   BankResponse balanceEnquiry(EnquiryRequest request);


   String nameEnquiry(EnquiryRequest request);


   BankResponse creditAccount(CreditDebitRequest request);


   BankResponse debitAccount(CreditDebitRequest request);


   BankResponse transfer(TransferRequest request);

   BankResponse login(LoginDto loginDto);
}
