package com.blogapp.bloggy.controller;

import com.blogapp.bloggy.payload.PostDto;
import com.blogapp.bloggy.payload.PostResponse;
import com.blogapp.bloggy.service.PostService;
import com.blogapp.bloggy.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    @SecurityRequirement(name = "Bear Authentication")
    @Operation(
            summary = "Create Post REST API"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //Include search in get route
    @GetMapping
    public PostResponse getPosts(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                 @RequestParam(value="sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false) String sortDir,
                                 @RequestParam(value="query",defaultValue = "" ,required = false) String query) {
        if (query.equalsIgnoreCase("")){
            return postService.getPosts(pageNo, pageSize,sortBy, sortDir);
        }
        else{
            return postService.searchPosts(query,pageNo, pageSize,sortBy, sortDir);
        }
    }

    @GetMapping("/user/{id}")
    public List<PostDto> getPostsByUser(@PathVariable Long id) {
        return postService.getPostsByUserId(id);
    }

    @GetMapping("/category/{id}")
    public List<PostDto> getPostsByCategory(@PathVariable(name="id") Long id){
        return postService.getPostsByCategoryId(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<PostDto> updatePost(@PathVariable("id") long id, @Valid @RequestBody PostDto post) {
        return ResponseEntity.ok(postService.updatePost(id, post));
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {

        postService.deletePost(id);
        return new ResponseEntity<>("Post has been deleted successfully!", HttpStatus.OK);
    }


}
