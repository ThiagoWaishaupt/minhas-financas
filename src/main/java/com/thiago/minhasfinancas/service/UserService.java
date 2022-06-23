package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.model.User;

import java.util.Optional;

public interface UserService {
    User authenticate(String email, String password);
    User saveUser(User user);
    void emailValidateExists(String email);
    Optional<User> searchUser(Long idUser);
}
