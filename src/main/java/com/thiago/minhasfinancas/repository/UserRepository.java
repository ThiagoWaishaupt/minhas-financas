package com.thiago.minhasfinancas.repository;

import com.thiago.minhasfinancas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);

}
