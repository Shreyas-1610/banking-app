/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.shreyas.bank.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    @Schema(
        name="User Account name"
    )
    private String accountName;
    @Schema(
        name="User Account Balance"
    )
    private BigDecimal accountBalance;
    @Schema(
        name="User Account Number"
    )
    private String accountNumber;
}
