package com.example.demo.Myconstant;

import com.example.demo.Reponsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MyConstant {
    @Autowired
    UserRepository userRepository;
    public static Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    public static final String MY_EMAIL = "eshop2611@gmail.com";

    // Replace password!!
    public static final String MY_PASSWORD = "bfpffkedfnnkokzw";

    // And receiver!

}
