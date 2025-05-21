package com.example.SpringSecDemo.filters;

import com.example.SpringSecDemo.token.JWTAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtValidationFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractJwtFromRequest(request);
        System.out.println("token-1:"+token);
        if(token != null){
            JWTAuthenticationToken authenticationToken = new JWTAuthenticationToken(token);
            System.out.println("authenticationToken:"+authenticationToken);
            Authentication authResult = authenticationManager.authenticate(authenticationToken);
            System.out.println("authResult:"+authResult);

            if (authResult.isAuthenticated())
                SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        System.out.println("filterChain.doFilter");
        filterChain.doFilter(request,response);

    }

    public String extractJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer"))
            return bearerToken.substring(7);

        return null;
    }
}
