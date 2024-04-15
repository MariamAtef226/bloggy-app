package com.blogapp.bloggy.security;

import com.blogapp.bloggy.entity.Comment;
import com.blogapp.bloggy.entity.Post;
import com.blogapp.bloggy.entity.Role;
import com.blogapp.bloggy.entity.User;
import com.blogapp.bloggy.exception.ResourceNotFoundException;
import com.blogapp.bloggy.repository.CommentRepository;
import com.blogapp.bloggy.repository.PostRepository;
import com.blogapp.bloggy.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserDetailsService userDetailsService,
                                   PostRepository postRepository,
                                   CommentRepository commentRepository,
                                   UserRepository userRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
        // get Jwt token from http request
        String token = getTokenFromRequest(request);
        // validate token
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            // get username
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // Perform authorization checks

                if (!isAuthorized(request, username)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
        }
        filterChain.doFilter(request, response);
        }catch (ResourceNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
            response.getWriter().flush(); // Ensure that the response is flushed
        }
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }


    private boolean isAuthorized(HttpServletRequest request, String username) {

            User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
            for (Role role : user.getRoles()) {
                if ("ROLE_ADMIN".equals(role.getName()))
                    return true;
            }

            // else if ordinary user
            if ("DELETE".equals(request.getMethod()) || "PUT".equals(request.getMethod()) || "PATCH".equals(request.getMethod())) {
                String[] pathParts = request.getRequestURI().split("/");
                if (pathParts.length == 5 && pathParts[3].equals("posts")) {
                    long postId = Long.parseLong(pathParts[4]);
                    return postBelongsToUser(postId, username);
                } else if (pathParts.length == 7 && pathParts[5].equals("comments")) {
                    long commentId = Long.parseLong(pathParts[6]);
                    return commentBelongsToUser(commentId, username);
                }
            }
            return true;

    }

    private boolean postBelongsToUser(long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", Long.toString(postId)));
        User user = userRepository.findByUsernameOrEmail(username,username).orElseThrow(()->new ResourceNotFoundException("User","username",username));
        if(post.getUser().getId() == user.getId())
            return true;
        return false;
    }

    private boolean commentBelongsToUser(long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", Long.toString(commentId)));
        User user = userRepository.findByUsernameOrEmail(username,username).orElseThrow(()->new ResourceNotFoundException("User","username",username));
        if(comment.getUser().getId() == user.getId())
            return true;
        return false;
    }
}
