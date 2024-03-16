package com.blogapp.bloggy.service.impl;

import com.blogapp.bloggy.entity.Comment;
import com.blogapp.bloggy.entity.Post;
import com.blogapp.bloggy.exception.BlogApiException;
import com.blogapp.bloggy.exception.ResourceNotFoundException;
import com.blogapp.bloggy.payload.CommentDto;
import com.blogapp.bloggy.payload.CommentResponse;
import com.blogapp.bloggy.repository.CommentRepository;
import com.blogapp.bloggy.repository.PostRepository;
import com.blogapp.bloggy.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", Long.toString(postId)));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public CommentResponse getCommentsByPostId(long postId, int pageNo, int pageSize, String sortBy, String sortDir) {
        // Check first if some post with this id exist or not:
        Post post = postRepository.findById((Long)postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", Long.toString(postId)));
        // Handling sorting:
        Sort sort = (sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create a pageable object that will handle both pagination & sorting
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        // Create a page instance
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        // get the content from that page --> entity not DTO
        List<Comment> listOfComments = comments.getContent();
        // convert to DTO
        List<CommentDto> content = listOfComments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        // Make a response object & return it
        return new CommentResponse(content, comments.getNumber(), comments.getSize(), comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
    }

    @Override
    public CommentDto getCommentById(long postId, long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", Long.toString(id)));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", Long.toString(postId)));
        if (comment.getPost().getId() == post.getId())
            return mapToDto(comment);
        else throw new BlogApiException(HttpStatus.BAD_REQUEST,"This Comment doesn't belong to this post!");
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", Long.toString(id)));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", Long.toString(postId)));
        if (comment.getPost().getId() != post.getId())
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"This Comment doesn't belong to this post!");

        if(commentDto.getBody()  != null){
            comment.setBody(commentDto.getBody());
        }
        if(commentDto.getName()  != null){
            comment.setName(commentDto.getName());
        }
        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
        // A Cleaner code can be using modelmapper package --> to be studied later on
    }

    @Override
    public void deleteComment(long postId, long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", Long.toString(id)));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", Long.toString(postId)));
        if (comment.getPost().getId() != post.getId())
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"This Comment doesn't belong to this post!");
        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        return mapper.map(comment,CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return mapper.map(commentDto,Comment.class);
    }
}
