package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Pager;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.service.CategoryService;
import com.thogwa.thogwa.backend.service.HeaderCategoryService;
import com.thogwa.thogwa.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/sort")
public class SortController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    HeaderCategoryService headerCategoryService;
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 8;
    private static final int[] PAGE_SIZES = {8, 12, 16, 18, 20};

//        @GetMapping()
//    public ModelAndView getProductPage(
//            @RequestParam(name = "sort", defaultValue = "higher-price") String sort ,
//            @RequestParam(name = "pageSize", defaultValue = "8") Optional<Integer>  pageSize,
//            @RequestParam(name = "page", defaultValue = "1") Optional<Integer> page) {
//
//        return filterProducts(sort,page,pageSize);
//    }
    @GetMapping("/product-filter")
    public ModelAndView filterProducts(@RequestParam(value = "sort", defaultValue = "lower-price") String sort,
                                       @RequestParam("pageSize") Optional<Integer> pageSize,
                                       @RequestParam("page") Optional<Integer> page) {
        var modelAndView = new ModelAndView("lowPrice");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        Sort.Direction sortDirection = null;
        if (sort.equals("higher-price")) {
            sortDirection = Sort.Direction.DESC;
        } else if (sort.equals("lower-price")) {
            sortDirection = Sort.Direction.ASC;
        }

        Sort sortObj = Sort.by(sortDirection, "price"); // Assuming 'price' is the field you want to sort by

        Pageable pageable = PageRequest.of(evalPage, evalPageSize, sortObj);

        Page<Product> products = productService.findAllPageableSort(pageable);
        List<Category> categories = categoryService.listCategories();

        if (evalPage >= products.getTotalPages()) {
            return new ModelAndView("redirect:/product-filter?sort=" + sort +
                    "&pageSize=" + evalPageSize +
                    "&page=" + (products.getTotalPages() - 1));
        }

        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages", products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("categories",categories);

        return modelAndView;
    }
}