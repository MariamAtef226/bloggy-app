package com.blogapp.bloggy.repository;

import com.blogapp.bloggy.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
