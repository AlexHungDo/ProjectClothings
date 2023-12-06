package com.example.demo.Config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static javax.management.Query.and;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/registration**").permitAll()
                        .requestMatchers("/indexadmin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/indexuser/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated()
                .and()
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true));

        return http.build();
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/css/**","/js/**","/images/**","/fonts/**","/plugins/**",
                "/bootstrap.dist/**","/Category/**");
    }
}