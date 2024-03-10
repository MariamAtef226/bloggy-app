package com.blogapp.bloggy.service.impl;

import com.blogapp.bloggy.entity.Role;
import com.blogapp.bloggy.entity.User;
import com.blogapp.bloggy.exception.BlogApiException;
import com.blogapp.bloggy.payload.LoginDto;
import com.blogapp.bloggy.payload.SignupDto;
import com.blogapp.bloggy.repository.RoleRepository;
import com.blogapp.bloggy.repository.UserRepository;
import com.blogapp.bloggy.security.JwtTokenProvider;
import com.blogapp.bloggy.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String register(SignupDto signupDto) {
        // check if mail or username exists in database or not
        if(userRepository.existsByUsername(signupDto.getUsername())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "This username is already taken");
        }
        if(userRepository.existsByEmail(signupDto.getEmail())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"This email is already taken");
        }
        User user = new User();
        user.setName(signupDto.getName());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setUsername(signupDto.getUsername());
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER").get();
        roles.add(role);
        System.out.println(roles);
        user.setRoles(roles);
        userRepository.save(user);
        return "User is successfully registered!";


    }
}
