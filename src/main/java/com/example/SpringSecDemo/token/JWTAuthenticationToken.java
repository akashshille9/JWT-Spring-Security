package com.example.SpringSecDemo.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public JWTAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    public String getToken() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return null;
    }
    @Override
    public Object getPrincipal() {
        return null;
    }
}
