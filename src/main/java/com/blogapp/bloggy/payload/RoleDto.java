package com.blogapp.bloggy.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class RoleDto {
    private long id;
    @NotEmpty(message = "Name can't be null or empty")
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
