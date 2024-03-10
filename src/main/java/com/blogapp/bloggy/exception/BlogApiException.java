package com.blogapp.bloggy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
public class BlogApiException extends RuntimeException{
    private HttpStatus status;
    private String message;

}
