package com.blogapp.bloggy.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String username;
    @Size(min=8,message = "Password length should be greater than or equal 8")
    @NotEmpty
    private String password;
    @Email
    @NotEmpty
    private String email;
}
