package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;

public interface UserService {
     User save(User user);
     void delete(int userId);
}
