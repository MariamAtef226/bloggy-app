package com.blogapp.bloggy.controller;

import com.blogapp.bloggy.entity.User;
import com.blogapp.bloggy.payload.JwtAuthResponseDto;
import com.blogapp.bloggy.payload.LoginDto;
import com.blogapp.bloggy.payload.SignupDto;
import com.blogapp.bloggy.payload.UserDto;
import com.blogapp.bloggy.service.AuthService;
import com.blogapp.bloggy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;
    private UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        String email = authService.getUsername(token);
        UserDto user = userService.getUserByUsername(email);

        JwtAuthResponseDto jwtAuthResponseDto = new JwtAuthResponseDto();
        jwtAuthResponseDto.setAccessToken(token);
        jwtAuthResponseDto.setUsername(user.getUsername()); // Set the username
        jwtAuthResponseDto.setName(user.getName());
        jwtAuthResponseDto.setUserId(user.getId());
        return ResponseEntity.ok(jwtAuthResponseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupDto signupDto) {
        String response = authService.register(signupDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
