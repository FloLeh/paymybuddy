package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
     void save(User user) throws Exception;
}
