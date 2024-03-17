package com.blogapp.bloggy.service.impl;

import com.blogapp.bloggy.entity.Category;
import com.blogapp.bloggy.entity.Post;
import com.blogapp.bloggy.entity.User;
import com.blogapp.bloggy.exception.ResourceNotFoundException;
import com.blogapp.bloggy.payload.PostDto;
import com.blogapp.bloggy.payload.PostResponse;
import com.blogapp.bloggy.repository.CategoryRepository;
import com.blogapp.bloggy.repository.PostRepository;
import com.blogapp.bloggy.repository.UserRepository;
import com.blogapp.bloggy.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "id", Long.toString(postDto.getCategoryId())));
        User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", Long.toString(postDto.getUserId())));
        Post post = mapper.map(postDto,Post.class);
        // set skipped attributes
        post.setCategory(category);
        post.setUser(user);
        Post newPost = postRepository.save(post); // save to database
        return mapper.map(newPost,PostDto.class);  // convert returned entity to dto & return it to client
    }

    @Override
    public PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Handling sorting:
        Sort sort = (sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create a pageable object that will handle both pagination & sorting
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        // Create a page instance
        Page<Post> posts = postRepository.findAll(pageable);
        // get the content from that page --> entity not DTO
        List<Post> listOfPosts = posts.getContent();
        // convert to DTO
        List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        // Make a response object & return it
        return new PostResponse(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
    }

    @Override
    public PostDto getPostById(long id) {
        // Get by id or throw exception if not found
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", Long.toString(id)));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", Long.toString(id)));
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "id", Long.toString(postDto.getCategoryId())));
        // User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", Long.toString(postDto.getUserId())));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        // post.setUser(user);  // we can't chane post to belong to another user

        Post updatedPost = postRepository.save(post);
        return mapToDto(post);
        // A Cleaner code can be using modelmapper package --> to be studied later on
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", Long.toString(id)));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategoryId(Long categoryId) {
        // Check if this category exist or not
        categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",Long.toString(categoryId)));
        // get posts by cat id
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map((post)->mapToDto(post)).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByUserId(Long userId) {
        // Check if this user exist or not
        userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",Long.toString(userId)));
        // get posts by user id
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map((post)->mapToDto(post)).collect(Collectors.toList());
    }

    @Override
    public PostResponse searchPosts(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        // Handling sorting:
        Sort sort = (sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create a pageable object that will handle both pagination & sorting
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        // Create a page instance
        Page<Post> posts = postRepository.searchPosts(query,pageable);
        // get the content from that page --> entity not DTO
        List<Post> listOfPosts = posts.getContent();
        // convert to DTO
        List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        // Make a response object & return it
        return new PostResponse(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

    }


    private PostDto mapToDto(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);
        postDto.setUserName(post.getUser().getName());
        postDto.setCategoryName(post.getCategory().getName());
        return postDto;
    }

    private Post mapToEntity(PostDto postDto) {
        return mapper.map(postDto, Post.class);
    }
}
