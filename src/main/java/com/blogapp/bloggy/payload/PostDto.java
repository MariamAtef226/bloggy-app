package com.blogapp.bloggy.payload;

import com.blogapp.bloggy.entity.Category;
import com.blogapp.bloggy.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Schema(
        description = "PostDto Model Information"
)
public class PostDto {
    private long id;
//
//    @Schema(
//            description = "Post Title"
//    )
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    @NotEmpty
    @Size(max = 2000, message = "Post content's length shouldn't exceed 2000 characters")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<CommentDto> comments;
    private Long userId;
    private Long categoryId;
    private String userName;
    private String categoryName;
}
