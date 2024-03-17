package com.blogapp.bloggy.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private long id;
    @NotEmpty
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
