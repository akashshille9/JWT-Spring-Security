package com.example.SpringSecDemo.service;

import com.example.SpringSecDemo.entity.UserAuthEntity;
import com.example.SpringSecDemo.repository.UserAuthEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class UserAuthEntityService implements UserDetailsService {

    @Autowired
    UserAuthEntityRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    public void saveUserDetails(UserAuthEntity userDetails) {
        repository.save(userDetails);
    }

    @GetMapping("/users")
    public String getUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication: "+authentication);
        return "Fetched user details successfully";
    }
}
