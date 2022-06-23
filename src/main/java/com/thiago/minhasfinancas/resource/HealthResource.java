package com.thiago.minhasfinancas.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthResource {

    @GetMapping
    public String health(){
        return "Hello World!";
    }

}
