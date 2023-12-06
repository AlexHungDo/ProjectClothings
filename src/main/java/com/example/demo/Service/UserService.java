package com.example.demo.Service;


import com.example.demo.Untity.User;
import com.example.demo.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);
    User admin();
    List<User> getAll();
}