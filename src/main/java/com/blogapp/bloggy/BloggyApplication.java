package com.blogapp.bloggy;

import com.blogapp.bloggy.entity.Comment;
import com.blogapp.bloggy.entity.Post;
import com.blogapp.bloggy.entity.Role;
import com.blogapp.bloggy.payload.CommentDto;
import com.blogapp.bloggy.payload.PostDto;
import com.blogapp.bloggy.repository.RoleRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Bloggy App REST APIs",
                description = "This is the documentation from Spring Boot Bloggy App",
                version = "v1.0",
                contact = @Contact(
                        name = "Mariam Atef",
                        email = "mariamatef226@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot external description"
                // url="" external documentation source
        )
)
public class BloggyApplication implements CommandLineRunner {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modMap = new ModelMapper();
        modMap.addMappings(new PropertyMap<Post, PostDto>() {
            protected void configure() {
                skip().setUserName(null);
                skip().setCategoryName(null);
            }
        });
        modMap.addMappings(new PropertyMap<Comment, CommentDto>() {
            protected void configure() {
                skip().setUserName(null);
            }
        });
        return modMap;
    }
    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(BloggyApplication.class, args);
        System.out.println("Bloggy app is running ...");
    }

    @Override
    public void run(String ...args)throws Exception{

    }

}
