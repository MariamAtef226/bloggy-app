package com.blogapp.bloggy.service.impl;

import com.blogapp.bloggy.entity.Category;
import com.blogapp.bloggy.entity.Role;
import com.blogapp.bloggy.entity.User;
import com.blogapp.bloggy.exception.ResourceNotFoundException;
import com.blogapp.bloggy.payload.CategoryDto;
import com.blogapp.bloggy.payload.UserDto;
import com.blogapp.bloggy.repository.RoleRepository;
import com.blogapp.bloggy.repository.UserRepository;
import com.blogapp.bloggy.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ModelMapper mapper;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper mapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository =roleRepository;
        this.mapper = mapper;
    }
    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", Long.toString(id)));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String username) {
      User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new ResourceNotFoundException("User", "username or email", username));
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = users.stream().map(user -> mapper.map(user,UserDto.class)).collect(Collectors.toList());
        return usersDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapper.map(userDto,User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER").get();
        roles.add(role);
        user.setRoles(roles);
        User newUser = userRepository.save(user);
        newUser.setPassword("");
        return mapper.map(newUser,UserDto.class);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",Long.toString(userId)));
        user.setName(userDto.getName()); // user is only allowed to change their name
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        User savedUser = userRepository.save(user);
        return mapper.map(savedUser,UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",Long.toString(userId)));
        userRepository.delete(user);
    }
}
