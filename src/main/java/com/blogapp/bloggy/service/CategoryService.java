package com.blogapp.bloggy.service;

import com.blogapp.bloggy.payload.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);
    List<CategoryDto> getCategories();
    CategoryDto getCategory(Long categoryId);
    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);
    void deleteCategory(Long categoryId);
}
