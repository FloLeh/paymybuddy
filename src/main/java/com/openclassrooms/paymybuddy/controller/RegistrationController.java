package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/register")
    public User registerUser(User user, HttpServletRequest request, HttpServletResponse response) throws Exception { // Ne fonctionne pas avec un requestBody
        try {
            userService.save(user);
        } catch (Exception e) {
            log.error("Error saving user", e);
            response.sendRedirect(response.encodeRedirectURL("/register?error=true"));
        }
        request.login(user.getEmail(), user.getPassword());
        response.sendRedirect(response.encodeRedirectURL("/transfer"));
        return user;
    }

}
