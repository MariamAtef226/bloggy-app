package com.blogapp.bloggy.service;

import com.blogapp.bloggy.payload.UserDto;

public interface UserService {
    UserDto getUserById(long id);
    UserDto getUserByUsername(String username);

}
