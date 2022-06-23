package com.thiago.minhasfinancas.resource;

import com.thiago.minhasfinancas.exception.AuthenticateException;
import com.thiago.minhasfinancas.exception.BusinessRuleException;
import com.thiago.minhasfinancas.model.User;
import com.thiago.minhasfinancas.model.UserDTO;
import com.thiago.minhasfinancas.service.ReleaseService;
import com.thiago.minhasfinancas.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserResource {

    private UserService userService;
    private ReleaseService releaseService;

    @PostMapping
    public ResponseEntity save( @RequestBody UserDTO userDTO){ //The annotation forces the json fields to be equal to DTO fields
        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        try{
            User userResult = userService.saveUser(user);
            return new ResponseEntity(userResult, HttpStatus.CREATED);
        } catch(BusinessRuleException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody UserDTO userDTO){
        try{
            User userResult = userService.authenticate(userDTO.getEmail(), userDTO.getPassword());
            return new ResponseEntity(userResult, HttpStatus.OK);
        } catch (AuthenticateException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity balance(@PathVariable("id") Long id){

        Optional<User> user = userService.searchUser(id);
        if(user.isEmpty()){
            return new ResponseEntity("User not found.", HttpStatus.BAD_REQUEST);
        }

        BigDecimal balance = releaseService.getBalance(id);

        return new ResponseEntity(balance, HttpStatus.OK);
    }

}
