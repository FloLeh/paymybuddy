package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.RegisterUserRequest;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterUserRequest registerUserRequest, BindingResult result, Model model) {
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            model.addAttribute("errorMessages", errors);
            return "register";
        }

        try {
            UserEntity user = new UserEntity(registerUserRequest.username(), registerUserRequest.email(), registerUserRequest.password());
            userService.createUser(user);
        } catch (Exception e) {
            model.addAttribute("errorMessages", List.of(e.getMessage()));
            return "register";
        }

        return "redirect:/login";
    }

}
