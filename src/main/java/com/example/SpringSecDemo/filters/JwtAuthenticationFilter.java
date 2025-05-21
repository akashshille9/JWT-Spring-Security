package com.example.SpringSecDemo.filters;

import com.example.SpringSecDemo.entity.LoginRequest;
import com.example.SpringSecDemo.utility.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
    
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    this.authenticationManager=authenticationManager;
    this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(!request.getServletPath().equals("/generate-token")){
            filterChain.doFilter(request,response);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authResult = authenticationManager.authenticate(authToken);

        System.out.println("authResult.isAuthenticated():"+authResult.isAuthenticated());
        if (authResult.isAuthenticated()){
            String token = jwtUtil.generateToken(authResult.getName(), 5);
            response.setHeader("Authorization","Bearer "+token);
        }
    }
}
