/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.shreyas.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyas.bank.entity.Transaction;

/**
 *
 * @author Shreyas
 */
public interface TransactionRepository extends JpaRepository<Transaction, String>{

}
