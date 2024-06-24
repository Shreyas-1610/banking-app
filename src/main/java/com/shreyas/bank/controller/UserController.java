/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.shreyas.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyas.bank.dto.BankResponse;
import com.shreyas.bank.dto.CreditDebitRequest;
import com.shreyas.bank.dto.EnquiryRequest;
import com.shreyas.bank.dto.LoginDto;
import com.shreyas.bank.dto.TransferRequest;
import com.shreyas.bank.dto.UserRequest;
import com.shreyas.bank.service.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 *
 * @author Shreyas
 */
@RestController
@RequestMapping("/api/user")
@Tag(name="User Account Management APIs")
public class UserController {
    @Autowired
    UserService userService;
    @Operation(
        summary="Create new User Account",
        description="creating new user and assigning an account id"
    )
    @ApiResponse(
        responseCode="201",
        description="HTTP STATUS 201 CREATED"
    )

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    @Operation(
        summary="Balance enquiry",
        description="Check how much money user has"
    )
    @ApiResponse(
        responseCode="200",
        description="HTTP STATUS 200 SUCCESS"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }


    @Operation(
        summary="Name enquiry",
        description="Check the name of user"
    )
    @ApiResponse(
        responseCode="200",
        description="HTTP STATUS 200 SUCCESS"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }

    @Operation(
        summary="Credit into an account",
        description="crediting money into an user account"
    )
    @ApiResponse(
        responseCode="201",
        description="HTTP STATUS 201 CREATED"
    )
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }
    @Operation(
        summary="Debit to an account",
        description="debiting money to an user account"
    )
    @ApiResponse(
        responseCode="201",
        description="HTTP STATUS 201 CREATED"
    )
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }

    @Operation(
        summary="Transfer between two users",
        description="Transfering money between 2 existing accounts"
    )
    @ApiResponse(
        responseCode="201",
        description="HTTP STATUS 201 CREATED"
    )
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }
}
