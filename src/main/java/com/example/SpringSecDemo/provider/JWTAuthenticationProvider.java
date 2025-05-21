package com.example.SpringSecDemo.provider;

import com.example.SpringSecDemo.token.JWTAuthenticationToken;
import com.example.SpringSecDemo.utility.JWTUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthenticationProvider(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JWTAuthenticationToken) authentication).getToken();
        System.out.println("token:"+token);
        String username=jwtUtil.validateAndExtractUsername(token);
        System.out.println("username:"+username);
        if (username == null)
            throw new BadCredentialsException("Invalid JWT Token");

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println("userDetails:"+userDetails);
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
