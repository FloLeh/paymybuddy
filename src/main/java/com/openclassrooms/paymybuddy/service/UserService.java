package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.UserNotUpdatedException;
import com.openclassrooms.paymybuddy.model.UserEntity;

public interface UserService {
     void createUser(UserEntity user) throws Exception;
     void updateUser(UserEntity user) throws UserNotUpdatedException;
     UserEntity findByEmail(String email);
     UserEntity findById(Integer id);
     void addConnection(String currentUserEmail, String connectionEmail);
}
