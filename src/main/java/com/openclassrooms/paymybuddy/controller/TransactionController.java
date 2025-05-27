package com.openclassrooms.paymybuddy.controller;

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
        List<TransactionEntity> transactions = transactionService.getTransactionsByUser(user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("connections", user.getConnections());
        model.addAttribute("active", "transfer");
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Integer receiverId,
                         @RequestParam String description,
                         @RequestParam Double amount,
                         Model model,
                         Authentication auth) {
        UserEntity sender = userService.findByEmail(auth.getName());
        UserEntity receiver = userService.findById(receiverId);
        TransactionEntity transaction = new TransactionEntity();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        List<TransactionEntity> transactions = transactionService.getTransactionsByUser(sender);
        model.addAttribute("transactions", transactions);
        model.addAttribute("connections", sender.getConnections());
        transactionService.addTransaction(transaction);
        model.addAttribute("active", "transfer");
        return "transfer";
    }
}
