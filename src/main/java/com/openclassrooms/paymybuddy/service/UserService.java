package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserEntity;

public interface UserService {
     void createUser(UserEntity user) throws Exception;
     UserEntity findByEmail(String email);
     UserEntity findById(Integer id);
     String addConnection(String userEmail, String connectionEmail);
}
