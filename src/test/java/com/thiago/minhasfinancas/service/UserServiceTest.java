package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.exception.BusinessRuleException;
import com.thiago.minhasfinancas.model.User;
import com.thiago.minhasfinancas.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @SpyBean
    UserServiceImpl userService;

    @Test
    void emailValidateExists() {

        when(userRepository.existsByEmail("emailValido@email.com")).thenReturn(false);

        userService.emailValidateExists("emailValido@email.com");

        Assertions.assertDoesNotThrow(() -> userService.emailValidateExists("emailValido@email"));
    }

    @Test
    void emailValidateExistsError() {

        when(userRepository.existsByEmail("emailRegistrado@email.com")).thenReturn(true);

        Assertions.assertThrows(BusinessRuleException.class, () -> userService.emailValidateExists("emailRegistrado@email.com"));
    }

    @Test
    void authenticateWithSuccess(){
        Optional<User> user = Optional.of(User.builder()
                .name("Thiago")
                .email("thiago@email.com")
                .password("12345")
                .build());

        when(userRepository.findByEmailAndPassword("emailValido@email.com", "senhaValida")).thenReturn(user);

        userService.authenticate("emailValido@email.com", "senhaValida");

        Assertions.assertDoesNotThrow(() -> userService.authenticate("emailValido@email.com", "senhaValida"));
    }

    @Test
    void saveUserWithSuccess(){
        User user = User.builder()
                .id(1L)
                .name("Thiago")
                .email("thiago@email.com")
                .password("12345")
                .build();

        doNothing().when(userService).emailValidateExists(anyString());

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(new User());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), 1L);
        Assertions.assertEquals(result.getName(), "Thiago");
        Assertions.assertEquals(result.getEmail(), "thiago@email.com");
        Assertions.assertEquals(result.getPassword(), "12345");
    }

    @Test
    void saveUserWithError(){
        User user = User.builder()
                .email("emailRegistrado@email.com")
                .build();

        doThrow(BusinessRuleException.class).when(userService).emailValidateExists("emailRegistrado@email.com");

        Assertions.assertThrows(BusinessRuleException.class, () -> userService.saveUser(user));
        verify(userRepository, never()).save(user);
    }

}