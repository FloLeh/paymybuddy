package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profilePage(Model model, Authentication auth) {
        String email = auth.getName();
        UserEntity user = userService.findByEmail(email);
        model.addAttribute("active", "profile");
        model.addAttribute("email", email);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        return "profile";
    }

    @GetMapping("/connections")
    public String connectionsPage(Model model) {
        model.addAttribute("active", "connections");
        return "connections";
    }

    @PostMapping("/connections")
    public void connectionsSubmit(@RequestParam String email, Authentication auth, Model model) {
        String submitStatus = userService.addConnection(auth.getName(), email);
        model.addAttribute("submitStatus", submitStatus);
        model.addAttribute("active", "connections");
    }

}
