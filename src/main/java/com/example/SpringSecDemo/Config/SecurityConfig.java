package com.example.SpringSecDemo.Config;

import com.example.SpringSecDemo.filters.JwtAuthenticationFilter;
import com.example.SpringSecDemo.filters.JwtValidationFilter;
import com.example.SpringSecDemo.provider.JWTAuthenticationProvider;
import com.example.SpringSecDemo.utility.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JWTUtil jwtUtil,UserDetailsService userDetailsService ) {
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder ());
        return provider;
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider(){
        return new JWTAuthenticationProvider(jwtUtil,userDetailsService);
    }
    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, JWTUtil jwtUtil) throws Exception {

        JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(authenticationManager, jwtUtil);
        JwtValidationFilter jwtValidationFilter = new JwtValidationFilter(authenticationManager);

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user-register").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf->csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter,JwtAuthenticationFilter.class);
//                .headers(httpSecurityHeadersConfigurer -> {
//                    httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> {
//                        frameOptionsConfig.disable();
//                    });
//                })
              return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider(),jwtAuthenticationProvider()));
    }
}
