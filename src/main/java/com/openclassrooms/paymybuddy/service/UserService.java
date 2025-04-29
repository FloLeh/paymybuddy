package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;

public interface UserService {
     User save(User user) throws Exception;
     void delete(int userId);
}
