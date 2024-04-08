package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.HeaderCategory;
import com.thogwa.thogwa.backend.model.Pager;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.repository.CategoryRepository;
import com.thogwa.thogwa.backend.repository.HeaderCategoryRepository;
import com.thogwa.thogwa.backend.service.CategoryService;
import com.thogwa.thogwa.backend.service.HeaderCategoryService;
import com.thogwa.thogwa.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class Admin {
//    @GetMapping("/admin")
//    public String adminHome () {
//        return "admin/adminHome";
//    }
    @Autowired
    CategoryService categoryService;
    @Autowired
    public ProductService productService;
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 8;
    private static final int[] PAGE_SIZES = {8, 12, 16, 18, 20};

    @GetMapping("/dashboard")
    public String adminHome() {
        return "admin/index";
    }
    @Autowired
    private HeaderCategoryService headerCategoryService;
    @GetMapping("products")
    public String getProducts(Model model) {
        model.addAttribute("categories",categoryService.listCategories());
        model.addAttribute("headers",headerCategoryService.findAll());
        model.addAttribute("product",new Product());

        return "admin/addProducts";
    }
    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("categories",categoryService.listCategories());
        model.addAttribute("headers",headerCategoryService.findAll());
        model.addAttribute("product",product);

        return "admin/updateProduct";
    }
    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") Product productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes, Principal principal,@PathVariable Long id) {
        try {
            productService.update(imageProduct,productDto,id);
            redirectAttributes.addFlashAttribute("success","Add new product successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("error","Failed to add new product!");
        }
        return "redirect:/admin/view";
    }
    @GetMapping("/specialPrice/{id}")
    public String discountForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("categories",categoryService.listCategories());
        model.addAttribute("headers",headerCategoryService.findAll());
        model.addAttribute("product",product);

        return "admin/specialPrice";
    }
    @PostMapping("/specialPrice/{id}")
    public String addDiscount(@RequestParam(name = "specialPrice") BigDecimal specialPrice,
                              RedirectAttributes redirectAttributes, @PathVariable Long id) {
        try {
            productService.addDiscount(specialPrice,id);
            redirectAttributes.addFlashAttribute("success","Add new product successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("error","Failed to add new product!");
        }
        return "redirect:/admin/view";
    }

    @GetMapping("/admin/view")
    public ModelAndView showProductsAdmin(@RequestParam("pageSize") Optional<Integer> pageSize,
                                          @RequestParam("page") Optional<Integer> page){
//        if(principal == null) {
//            return new ModelAndView("redirect:/admin/pages-login");
//        }
        var modelAndView = new ModelAndView("admin/viewProducts");
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // If a requested parameter is null or less than 1,
        // return the initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        var products = productService.findAllPageableByAdmin(evalPage, evalPageSize);
        // Check if the requested page is out of bounds
        if (evalPage >= products.getTotalPages()) {
            // Redirect to the last available page
            return new ModelAndView("redirect:/product/?pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
        }
        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages",products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }
    @GetMapping("/admin/login")
    public String LoginForm() {
        return "admin/pages-login";
    }
    @GetMapping("/admin/addCategory")
    public String adminCategory() {
        return "admin/addCategory";
    }
    @GetMapping("/admin/addPost")
    public String adminPost() {
        return "admin/createBlog";
    }
    @GetMapping("/admin/addHeader")
    public String adminHeader() {
        return "admin/addHeader";
    }
//    @GetMapping("/admin/orders")
//    public String viewOrders() {
//        return "admin/orders";
//    }


}
