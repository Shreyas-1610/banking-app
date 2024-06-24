/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.shreyas.bank.utils;

import java.time.Year;

/**
 *
 * @author Shreyas
 */
public class AccountUtils {
    public static String generateAccountNumber(){

    
    Year currentYear=Year.now();
    int min=100000;
    int max=999999;

    int randNumber = (int)Math.floor(Math.random() * (max- min + 1) +min);

    //convert curr year and randNumber in string and concatenate
    String year=String.valueOf(currentYear);
    String randomNumber= String.valueOf(randNumber);

    StringBuilder accountNumber= new StringBuilder();

    return accountNumber.append(year).append(randomNumber).toString();
    }

    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="Account already exists";

    public static final String ACCOUNT_CREATION_SUCCESS="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Account successfully created";

    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="User with provided Account Number does not exist";

    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_SUCCESS="User Account Found";

    public static final String ACCOUNT_CREDITED_SUCCESS ="005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Amount credited successfully";

    public static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance to withdraw";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE="007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE="Amount debited successfully";

    public static final String TRANSFER_SUCCESSFUL_CODE="008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE="Transfer Successful";

}
