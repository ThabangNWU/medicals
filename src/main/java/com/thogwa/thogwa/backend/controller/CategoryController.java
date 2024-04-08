package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.common.ApiResponse;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.HeaderCategory;
import com.thogwa.thogwa.backend.repository.HeaderCategoryRepository;
import com.thogwa.thogwa.backend.service.CategoryService;
import com.thogwa.thogwa.backend.service.HeaderCategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class CategoryController {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HeaderCategoryService headerCategoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    @GetMapping("categories")
    public String getCategories(Model model) {
        LOGGER.info("categories", headerCategoryService.findAll());
        model.addAttribute("categories", categoryService.listCategories());
        model.addAttribute("headers", headerCategoryService.findAll());
        return "admin/addCategory";
    }

    @GetMapping("category")
    @ResponseBody
    public List<Category> category(){
       return categoryService.listCategories();
    }
    @RequestMapping("category/findById")
    @ResponseBody
    public Optional<Category> findById(Integer id)
    {
        return categoryService.readCategory(id);
    }

    @PostMapping("category/addNew")
    public String addNew(Category category) {
        categoryService.saveCategory(category);
        LOGGER.info("categories", headerCategoryService.findAll());
        return "redirect:/admin/addCategory";
    }

    @GetMapping("/categories/{headerId}")
    public String viewCategoriesByHeader(@PathVariable Integer headerId, Model model) {
        List<Category> categories = categoryService.findByHeaderId(headerId);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedHeaderId", headerId);
        return "shop";
    }


}
