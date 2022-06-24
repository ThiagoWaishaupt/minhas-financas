package com.thiago.minhasfinancas.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiago.minhasfinancas.exception.AuthenticateException;
import com.thiago.minhasfinancas.exception.BusinessRuleException;
import com.thiago.minhasfinancas.model.User;
import com.thiago.minhasfinancas.model.UserDTO;
import com.thiago.minhasfinancas.service.ReleaseService;
import com.thiago.minhasfinancas.service.UserService;
import com.thiago.minhasfinancas.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserResource.class)
@AutoConfigureMockMvc
public class UserResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    ReleaseService releaseService;

    static final MediaType mediaTypeJson = MediaType.APPLICATION_JSON;

    @Test
    void authenticateUserWithSuccess() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .email("teste@email.com")
                .password("12345")
                .build();

        User userResult = User.builder()
                .id(1L)
                .email("teste@email.com")
                .password("12345")
                .build();

        when(userService.authenticate(userDTO.getEmail(), userDTO.getPassword())).thenReturn(userResult);

        String json = new ObjectMapper().writeValueAsString(userDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                .post(Constants.USER_BASE_URL.concat(Constants.USER_AUTHENTICATE))
                                                .accept(mediaTypeJson)
                                                .contentType(mediaTypeJson)
                                                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(userResult.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(userResult.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(userResult.getEmail())
                );
    }

    @Test
    void authenticateUserWithError() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .email("teste@email.com")
                .password("12345")
                .build();

        when(userService.authenticate(userDTO.getEmail(), userDTO.getPassword())).thenThrow(AuthenticateException.class);

        String json = new ObjectMapper().writeValueAsString(userDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(Constants.USER_BASE_URL.concat(Constants.USER_AUTHENTICATE))
                .accept(mediaTypeJson)
                .contentType(mediaTypeJson)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveUserWithSuccess() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .email("teste@email.com")
                .password("12345")
                .build();

        User userResult = User.builder()
                .id(1L)
                .email("teste@email.com")
                .password("12345")
                .build();

        when(userService.saveUser(any(User.class))).thenReturn(userResult);

        String json = new ObjectMapper().writeValueAsString(userDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(Constants.USER_BASE_URL)
                .accept(mediaTypeJson)
                .contentType(mediaTypeJson)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(userResult.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(userResult.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(userResult.getEmail())
                );
    }

    @Test
    void saveUserWithError() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .email("teste@email.com")
                .password("12345")
                .build();

        when(userService.saveUser(any(User.class))).thenThrow(BusinessRuleException.class);

        String json = new ObjectMapper().writeValueAsString(userDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(Constants.USER_BASE_URL)
                .accept(mediaTypeJson)
                .contentType(mediaTypeJson)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
