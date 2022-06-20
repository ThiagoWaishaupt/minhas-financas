package com.thiago.minhasfinancas.repository;

import com.thiago.minhasfinancas.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    @Mock
    UserRepository userRepository;

    @Test
    void verifyEmailExists() {
        User user = User.builder()
                .name("Thiago")
                .email("thiago@email.com")
                .password("12345")
                .build();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        boolean result = userRepository.existsByEmail(user.getEmail());

        Assertions.assertTrue(result);
    }

    @Test
    void verifyEmailNotExists() {
        User user = User.builder()
                .name("Thiago")
                .email("thiago@email.com")
                .password("12345")
                .build();

        when(userRepository.existsByEmail("emailNaoRegistrado")).thenReturn(false);

        boolean result = userRepository.existsByEmail(user.getEmail());

        Assertions.assertFalse(result);
    }

    @Test
    void verifyLoginSuccessfully(){

        Optional<User> user = Optional.of(User.builder()
                .name("Thiago")
                .email("thiago@email.com")
                .password("12345")
                .build());

        when(userRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword())).thenReturn(user);

        Optional<User> result = userRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(result.get().getName(), user.get().getName());
    }

    @Test
    void verifyLoginFailed(){

        Optional<User> user = Optional.of(User.builder()
                .name("Thiago")
                .email("thiago@email.com")
                .password("12345")
                .build());

        when(userRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword())).thenReturn(null);

        Optional<User> result = userRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());

        Assertions.assertNull(result);
    }
}