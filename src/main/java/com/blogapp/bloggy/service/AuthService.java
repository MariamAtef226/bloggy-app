package com.blogapp.bloggy.service;

import com.blogapp.bloggy.payload.LoginDto;
import com.blogapp.bloggy.payload.SignupDto;
import org.springframework.stereotype.Service;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(SignupDto signupDto);
    String getUsername(String token);


}
