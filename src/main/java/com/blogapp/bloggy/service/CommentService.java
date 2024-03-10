package com.blogapp.bloggy.service;

import com.blogapp.bloggy.payload.CommentDto;
import com.blogapp.bloggy.payload.CommentResponse;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto comment);
    CommentResponse getCommentsByPostId(long postId, int pageNo, int pageSize, String sortBy, String sortDir);
    CommentDto getCommentById(long postId, long id);
    CommentDto updateComment(long postId, long id, CommentDto commentDto);
    void deleteComment(long postId, long id);
}
