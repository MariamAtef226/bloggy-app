package com.blogapp.bloggy.service;

import com.blogapp.bloggy.payload.PostDto;
import com.blogapp.bloggy.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto post);
    PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(long id);
    PostDto updatePost(long id, PostDto postDto);
    void deletePost(long id);
    List<PostDto> getPostsByCategoryId(Long categoryId);
    PostResponse searchPosts(String query,  int pageNo, int pageSize, String sortBy, String sortDir);
}
