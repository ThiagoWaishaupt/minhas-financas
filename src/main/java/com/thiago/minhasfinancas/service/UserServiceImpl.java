package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.exception.AuthenticateException;
import com.thiago.minhasfinancas.exception.BusinessRuleException;
import com.thiago.minhasfinancas.model.User;
import com.thiago.minhasfinancas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User authenticate(String email, String password){
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);

        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new AuthenticateException("Email or password incorrect.");
        }
    }

    @Override
    @Transactional// open a transaction, save the user and do commit
    public User saveUser(User user) {
        emailValidateExists(user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void emailValidateExists(String email) {
        if(userRepository.existsByEmail(email)){
            throw new BusinessRuleException("There is a registered User with this email.");
        }
    }

    @Override
    public Optional<User> searchUser(Long idUser) {
        return userRepository.findById(idUser);
    }

}
