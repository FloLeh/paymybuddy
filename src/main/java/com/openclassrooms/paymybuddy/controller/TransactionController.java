package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmount;
import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping("/transfer")
    public String transferPage(Model model, Authentication auth) {
        UserEntity user = userService.findByEmail(auth.getName());
        List<TransactionRelativeAmount> transactionsList = transactionService.getTransactionsWithRelativeAmount(user);
        model.addAttribute("transactions", transactionsList);
        model.addAttribute("connections", user.getConnections());
        model.addAttribute("active", "transfer");
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@ModelAttribute TransactionCreateRequest transactionCreateRequest, Model model, Authentication auth) {
        TransactionEntity transaction = transactionService.createTransaction(transactionCreateRequest, auth.getName());
        List<TransactionRelativeAmount> transactionsList = transactionService.getTransactionsWithRelativeAmount(transaction.getSender());
        model.addAttribute("transactions", transactionsList);
        model.addAttribute("connections", transaction.getSender().getConnections());
        model.addAttribute("active", "transfer");
        return "transfer";
    }
}
