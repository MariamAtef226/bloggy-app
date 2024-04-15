package com.blogapp.bloggy.controller;

import com.blogapp.bloggy.payload.CommentDto;
import com.blogapp.bloggy.payload.CommentResponse;
import com.blogapp.bloggy.service.CommentService;
import com.blogapp.bloggy.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController {
    private CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/{post_id}/comments")
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<CommentDto> createComment(@PathVariable(name="post_id") long postId,@Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

//    @GetMapping("/{post_id}/comments")
//    public CommentResponse getCommentsByPostId(@PathVariable(name="post_id") long postId,
//                                               @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
//                                               @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
//                                               @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
//                                               @RequestParam(value="sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false) String sortDir){
//        return commentService.getCommentsByPostId(postId, pageNo, pageSize,sortBy, sortDir);
//    }
@GetMapping("/{post_id}/comments")
public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable(name="post_id") long postId){
    return new ResponseEntity<>(commentService.getCommentsByPostId(postId), HttpStatus.OK);
}

    @GetMapping("/{post_id}/comments/{comment_id}")
    public CommentDto getCommentById(@PathVariable(name="comment_id") long id, @PathVariable(name="post_id") long postId){
        return commentService.getCommentById(postId,id);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{post_id}/comments/{comment_id}")
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(name="post_id") long postId, @PathVariable(name="comment_id") long id, @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.updateComment(postId,id,commentDto), HttpStatus.OK);
    }
    @PreAuthorize("permitAll()")
    @DeleteMapping("{post_id}/comments/{comment_id}")
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<String> deleteComment(@PathVariable(name="post_id") long postId, @PathVariable(name="comment_id") long id){
        commentService.deleteComment(postId,id);
        return new ResponseEntity<>("Comment deleted!", HttpStatus.OK);
    }
}
