package com.blogapp.bloggy.service.impl;

import com.blogapp.bloggy.entity.Category;
import com.blogapp.bloggy.entity.Comment;
import com.blogapp.bloggy.entity.Post;
import com.blogapp.bloggy.exception.BlogApiException;
import com.blogapp.bloggy.exception.ResourceNotFoundException;
import com.blogapp.bloggy.payload.CategoryDto;
import com.blogapp.bloggy.payload.CommentDto;
import com.blogapp.bloggy.payload.CommentResponse;
import com.blogapp.bloggy.repository.CategoryRepository;
import com.blogapp.bloggy.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        return mapper.map(categoryRepository.save(category),CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        // convert to DTO
        List<CategoryDto> categoriesDto = categories.stream().map(category -> mapper.map(category,CategoryDto.class)).collect(Collectors.toList());
        // Make a response object & return it
        return categoriesDto;

    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        // Get by id or throw exception if not found
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", Long.toString(categoryId)));
        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", Long.toString(categoryId)));
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", Long.toString(categoryId)));
        categoryRepository.delete(category);
    }
}
