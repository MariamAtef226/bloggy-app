package com.blogapp.bloggy.payload;

import com.blogapp.bloggy.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;
    @NotEmpty(message = "Comment shouldn't be null or empty")
    @Size(max=1500,message = "Comment's size can't exceed 1500 characters")
    private String body;
    @NotEmpty(message = "Email of comment author shouldn't be null or empty")
    @Email
    private String email;
    @NotEmpty(message = "Name of comment author shouldn't be null or empty")
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
