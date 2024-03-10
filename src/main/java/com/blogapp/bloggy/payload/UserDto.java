package com.blogapp.bloggy.payload;

import com.blogapp.bloggy.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDto {

    private long id;
    private String name;
    @NotEmpty(message = "Username can't be null or empty")
    @Size(max=25,message = "Username's size can't exceed 25 characters")
    private String username;
    @NotEmpty(message = "Email can't be null or empty")
    @Email
    private String email;
    @NotEmpty(message = "Password can't be null or empty")
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Role> roles;
}
