package com.blogapp.bloggy.service;

import com.blogapp.bloggy.payload.CommentDto;
import com.blogapp.bloggy.payload.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto comment);
    List<CommentDto> getCommentsByPostId(long postId);
    CommentDto getCommentById(long postId, long id);
    CommentDto updateComment(long postId, long id, CommentDto commentDto);
    void deleteComment(long postId, long id);
}
