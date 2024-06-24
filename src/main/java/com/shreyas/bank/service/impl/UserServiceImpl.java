package com.shreyas.bank.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shreyas.bank.config.JwtTokenProvider;
import com.shreyas.bank.dto.AccountInfo;
import com.shreyas.bank.dto.BankResponse;
import com.shreyas.bank.dto.CreditDebitRequest;
import com.shreyas.bank.dto.EmailDetails;
import com.shreyas.bank.dto.EnquiryRequest;
import com.shreyas.bank.dto.LoginDto;
import com.shreyas.bank.dto.TransactionDto;
import com.shreyas.bank.dto.TransferRequest;
import com.shreyas.bank.dto.UserRequest;
import com.shreyas.bank.entity.Role;
import com.shreyas.bank.entity.User;
import com.shreyas.bank.repository.UserRepository;
import com.shreyas.bank.utils.AccountUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;
    @Override

    public BankResponse createAccount(UserRequest userRequest) {
        //creating an account=saving in db
        //check if user already exists

        if(userRepository.existsByEmail(userRequest.getEmail())){
           return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                        .accountInfo(null)
                        .build();
        }


        User newUser=User.builder()
            .firstName(userRequest.getFirstName())
            .lastName(userRequest.getLastName())
            .otherName(userRequest.getOtherName())
            .gender(userRequest.getGender())
            .address(userRequest.getAddress())
            .stateOfOrigin(userRequest.getStateOfOrigin())
            .accountNumber(AccountUtils.generateAccountNumber())
            .accountBalance(BigDecimal.ZERO)
            .email(userRequest.getEmail())
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .phoneNumber(userRequest.getPhoneNumber())
            .altNumber(userRequest.getAltNumber())
            .status("ACTIVE")
            .role(Role.valueOf("ROLE_ADMIN"))
            .build();

            
        User savedUser=userRepository.save(newUser);
        //send email alert
        EmailDetails emailDetails= EmailDetails.builder()
                            .recipient(savedUser.getEmail())
                            .subject("ACCOUNT CREATION")
                            .messageBody("Congratulations! Your account was created successfully. Welcome to Bank of Shreyas\nYour Account Details :\n Account Name:" +savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName()+"\nAccount Number:"+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())    
                        .build())
                .build();
    }

    public BankResponse login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You have been logged in")
                .recipient(loginDto.getEmail())
                .messageBody("You have been logged in your account. If not done by you contact our Customer service helpline number")
                .build();

        emailService.sendEmailAlert(loginAlert);
        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
    }


    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        //check if account number exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
            .accountInfo(null)
            .build();
        }
        User foundUser=userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
            .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
            .accountInfo(AccountInfo.builder()
                .accountBalance(foundUser.getAccountBalance())
                .accountNumber(request.getAccountNumber())
                .accountName(foundUser.getFirstName()+" "+ foundUser.getLastName()+" "+foundUser.getOtherName())
                .build())
        .build();
    }
    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " "+foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        //checking if account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
            .accountInfo(null)
            .build();
        }
        User userToCredit=userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);


        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
            .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
            .accountInfo(AccountInfo.builder()
                .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                .accountBalance(userToCredit.getAccountBalance())
                .accountNumber(request.getAccountNumber())
                .build())
            .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        //check if account exist
        //check if amount to withdraw is not more than current amount
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
            .accountInfo(null)
            .build();
        }

        User userToDebit= userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance=userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount=request.getAmount().toBigInteger();
        if(availableBalance.intValue()< debitAmount.intValue()){
            return BankResponse.builder()
                .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                .accountInfo(null)
                .build();
        }

        else{
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();

            transactionService.saveTransaction(transactionDto);

            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(request.getAccountNumber())
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        //get account to debit
        //check if debit amount is not more than current balance
        //debit the account
        //get account to credit
        //credit the account
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExist){
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
            .accountInfo(null)
            .build();
        }
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
        return BankResponse.builder()
        .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
        .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
        .accountInfo(null)
        .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        String sourceUsername= sourceAccountUser.getFirstName() + " "+ sourceAccountUser.getLastName()+ " " + sourceAccountUser.getOtherName();
        userRepository.save(sourceAccountUser);
        EmailDetails debitAlert = EmailDetails.builder()
                            .subject("DEBIT ALERT")
                            .recipient(sourceAccountUser.getEmail())
                            .messageBody("The sum of "+request.getAmount()+" has been deducted from your account! Your current balance is "+sourceAccountUser.getAccountBalance())
                            .build();

        emailService.sendEmailAlert(debitAlert);

        

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        
        //String recepientUserName=destinationAccountUser.getFirstName()+" " + destinationAccountUser.getLastName()+" "+ destinationAccountUser.getOtherName();
        userRepository.save(destinationAccountUser);
        EmailDetails creditAlert = EmailDetails.builder()
                            .subject("CREDIT ALERT")
                            .recipient(destinationAccountUser.getEmail())
                            .messageBody("The sum of "+request.getAmount()+" has been credited to your account from " + sourceUsername + "! Your current balance is"+destinationAccountUser.getAccountBalance())
                            .build();

        emailService.sendEmailAlert(creditAlert);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

            transactionService.saveTransaction(transactionDto);        
        
        return BankResponse.builder()
            .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
            .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
            .accountInfo(null)
            .build();
    }

}
