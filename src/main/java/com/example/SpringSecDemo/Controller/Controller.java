package com.example.SpringSecDemo.Controller;

import com.example.SpringSecDemo.entity.LoginRequest;
import com.example.SpringSecDemo.entity.UserAuthEntity;
import com.example.SpringSecDemo.service.UserAuthEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class Controller {

    @Autowired
    private UserAuthEntityService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user-register")
    public ResponseEntity<?> registerUser(@RequestBody UserAuthEntity userDetails){
        System.out.println(userDetails);
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        service.saveUserDetails(userDetails);
       return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @GetMapping("/users")
    public String generateToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication:-"+authentication);
         return "fetched user details successfully";
    }
}
