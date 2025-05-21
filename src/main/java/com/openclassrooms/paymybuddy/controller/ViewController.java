package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final TransactionService transactionService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/transfer")
    public String transferPage(Model model) {
        List<TransactionEntity> transactions = transactionService.getTransactions();
        model.addAttribute("transactions", transactions);
        return "transfer";
    }

}
