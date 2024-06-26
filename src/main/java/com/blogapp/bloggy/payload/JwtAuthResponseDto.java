package com.blogapp.bloggy.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponseDto {
    private String accessToken;
    private String tokenType = "bearer";
    private String username;
    private Long userId;
    private String name;

}
