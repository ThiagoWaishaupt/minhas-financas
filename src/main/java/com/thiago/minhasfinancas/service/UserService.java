package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.model.User;

public interface UserService {
    User authenticate(String email, String password);
    User saveUser(User user);
    void emailValidateExists(String email);
}
