package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.UpdatePasswordRequest;
import com.openclassrooms.paymybuddy.exceptions.BusinessException;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
        return "profile";
    }

    @PostMapping("/profile")
    public String profilePageSubmit(@Valid UpdatePasswordRequest updatePasswordRequest, BindingResult result, Model model, Authentication auth) {
        String email = auth.getName();
        UserEntity user = userService.findByEmail(email);
        model.addAttribute("active", "profile");
        model.addAttribute("email", email);
        model.addAttribute("username", user.getUsername());

        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            model.addAttribute("errorMessages", errors);
            return "profile";
        }

        try {
            userService.updatePassword(user, updatePasswordRequest.password());
            model.addAttribute("status" , "success");
        } catch (Exception e) {
            model.addAttribute("errorMessages", List.of(e.getMessage()));
        }
        return "profile";
    }

    @GetMapping("/connections")
    public String connectionsPage(Model model) {
        model.addAttribute("active", "connections");
        return "connections";
    }

    @PostMapping("/connections")
    public String connectionsSubmit(@RequestParam String email, Authentication auth, Model model) {
        model.addAttribute("active", "connections");
        try {
            userService.addConnection(auth.getName(), email);
            model.addAttribute("errorMessage" , "");
        } catch (BusinessException e) {
            model.addAttribute("errorMessage" , e.getMessage());
        }

        return "connections";
    }
}
