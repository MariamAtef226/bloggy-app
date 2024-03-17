package com.blogapp.bloggy.service;

import com.blogapp.bloggy.entity.User;
import com.blogapp.bloggy.payload.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(long id);
    UserDto getUserByUsername(String username);
    List<UserDto> getUsers();
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long userId, UserDto userDto);
    String deleteUser(Long userId);



}
