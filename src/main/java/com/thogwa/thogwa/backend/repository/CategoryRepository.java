package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Category findByCategoryName(String categoryName);
    List<Category> findByHeaderid(Integer headerId);
    Page<Category> findAll(Pageable pageable);
}
