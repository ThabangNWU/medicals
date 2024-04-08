package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.HeaderCategory;
import com.thogwa.thogwa.backend.repository.CategoryRepository;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.repository.HeaderCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

        @Autowired
        private CategoryRepository categoryrepository;
        @Autowired
        private HeaderCategoryRepository headerCategoryRepository;

        public List<Category> listCategories() {
            return categoryrepository.findAll();
        }

        public Category saveCategory(Category category) {
            return categoryrepository.save(category);
        }

        public Category readCategory(String categoryName) {
            return categoryrepository.findByCategoryName(categoryName);
        }

        public Optional<Category> readCategory(Integer categoryId) {
            return categoryrepository.findById(categoryId);
        }

        public void updateCategory(Integer categoryID, Category newCategory) {
            Category category = categoryrepository.findById(categoryID).get();
            category.setCategoryName(newCategory.getCategoryName());
            categoryrepository.save(category);
        }
        public List<Category> findByHeaderId(Integer id) {
            Optional<HeaderCategory> optionalHeaderCategory = headerCategoryRepository.findById(id);
            List<Category> categories = new ArrayList<>();
            List<HeaderCategory> headerCategoryList = headerCategoryRepository.findAll();
            for(HeaderCategory headerCategory : headerCategoryList ) {
                if(headerCategory.getId() == optionalHeaderCategory.get().getId()) {
                    for (Category categoryTemp : headerCategory.getCategoryList()) {
                        categories.add(categoryTemp);
                    }
                }
            }
            return categories;
        }
}

