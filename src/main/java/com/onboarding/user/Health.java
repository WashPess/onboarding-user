package com.onboarding.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {
    
    @GetMapping("/")
    public String index() {
        return "Health up";
    }
    
}
