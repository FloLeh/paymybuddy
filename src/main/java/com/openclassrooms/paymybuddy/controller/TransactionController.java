package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransactionCreateRequest;
import com.openclassrooms.paymybuddy.dto.TransactionRelativeAmount;
import com.openclassrooms.paymybuddy.exceptions.BusinessException;
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
        model.addAttribute("account", user.getAccount());
        model.addAttribute("active", "transfer");
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@ModelAttribute TransactionCreateRequest transactionCreateRequest, Model model, Authentication auth) {
        model.addAttribute("active", "transfer");
        UserEntity user = userService.findByEmail(auth.getName());

        try {
            transactionService.createTransaction(transactionCreateRequest, user);
            model.addAttribute("errorMessage" , "");
        } catch(BusinessException e) {
            model.addAttribute("errorMessage" , e.getMessage());
        }

        model.addAttribute("connections", user.getConnections());
        List<TransactionRelativeAmount> transactionsList = transactionService.getTransactionsWithRelativeAmount(user);
        model.addAttribute("transactions", transactionsList);
        model.addAttribute("account", user.getAccount());

        return "transfer";
    }
}
