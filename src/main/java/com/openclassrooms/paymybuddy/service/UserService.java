package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserEntity;

public interface UserService {
     void save(UserEntity user) throws Exception;
}
