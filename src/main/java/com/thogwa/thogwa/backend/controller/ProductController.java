package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.common.ApiResponse;
import com.thogwa.thogwa.backend.config.AppConstants;
import com.thogwa.thogwa.backend.dto.productdto.ProductResponse;
import com.thogwa.thogwa.backend.model.*;
import com.thogwa.thogwa.backend.repository.CategoryRepository;
import com.thogwa.thogwa.backend.repository.HeaderCategoryRepository;
import com.thogwa.thogwa.backend.service.CategoryService;
import com.thogwa.thogwa.backend.service.HeaderCategoryService;
import com.thogwa.thogwa.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
     @Autowired
     private HeaderCategoryRepository headerCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping()
    public ModelAndView displayProducts(
            @RequestParam(value = "min", required = false) Double minPrice,
            @RequestParam(value = "max", required = false) Double maxPrice,
            @RequestParam(value = "pageSize", defaultValue = "8") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @PathVariable(required = false) Integer id) {

        if (minPrice != null && maxPrice != null) {
            // If min and max prices are specified, redirect to the filtered products
            return showProducts(minPrice, maxPrice, Optional.of(pageSize), Optional.of(page));
        }

        // Otherwise, redirect to the regular product display
        return showProducts(null, null, Optional.of(pageSize), Optional.of(page));
    }


//    @GetMapping("/product")
//    public ModelAndView filterProducts(@RequestParam("sort") String sort,
//                                       @RequestParam("pageSize") Optional<Integer> pageSize,
//                                       @RequestParam("page") Optional<Integer> page) {
//        var modelAndView = new ModelAndView("shop");
//
//        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
//        int evalPage = page.filter(p -> p >= 1)
//                .map(p -> p - 1)
//                .orElse(INITIAL_PAGE);
//
//        Page<Product> products;
//
//        switch (sort) {
//            case "lower-price":
//                products = productService.filterByLowerPrice(evalPage, evalPageSize);
//                break;
//            case "higher-price":
//                products = productService.filterByHigherPrice(evalPage, evalPageSize);
//                break;
//            // Add more cases for other sorting options if needed
//
//            default:
//                products = productService.findAllPageable(evalPage, evalPageSize);
//                break;
//        }
//
//        if (evalPage >= products.getTotalPages()) {
//            return new ModelAndView("redirect:/product/?sort=" + sort + "&pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
//        }
//
//        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);
//
//        modelAndView.addObject("products", products);
//        modelAndView.addObject("totalItems", products.getTotalElements());
//        modelAndView.addObject("totalPages", products.getTotalPages());
//        modelAndView.addObject("selectedPageSize", evalPageSize);
//        modelAndView.addObject("pageSizes", PAGE_SIZES);
//        modelAndView.addObject("pager", pager);
//
//        return modelAndView;
//    }
    @GetMapping("/parameter")
    public ModelAndView showProducts(@RequestParam(value = "min", required = false) Double minPrice,
                                     @RequestParam(value = "max", required = false) Double maxPrice,
                                     @RequestParam("pageSize") Optional<Integer> pageSize,
                                     @RequestParam("page") Optional<Integer> page) {
        var modelAndView = new ModelAndView("shop");

        Page<Product> products;
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // If a requested parameter is null or less than 1,
        // return the initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        if (minPrice != null && maxPrice != null) {
            // Filtering by price range
            products = productService.findByPriceRange(minPrice, maxPrice, evalPage, evalPageSize);
        }
        else {
            // Display all products
            products = productService.findAllPageable(evalPage, evalPageSize);

        }


        // Rest of the logic remains the same as before
        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);
        List<HeaderCategory> headers = headerCategoryRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages", products.getTotalPages());
        modelAndView.addObject("selectedPageSize", pageSize.orElse(INITIAL_PAGE_SIZE));
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("headers", headers);
        modelAndView.addObject("categories", categories);


        return modelAndView;
    }

    @GetMapping("/filterMinMax")
    public ModelAndView showProductMinHigh(@RequestParam(value = "min", required = false) Double minPrice,
                                     @RequestParam(value = "max", required = false) Double maxPrice,
                                     @RequestParam("pageSize") Optional<Integer> pageSize,
                                     @RequestParam("page") Optional<Integer> page) {
        var modelAndView = new ModelAndView("minmax");

        Page<Product> products;
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // If a requested parameter is null or less than 1,
        // return the initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        if (minPrice != null && maxPrice != null) {
            // Filtering by price range
            products = productService.findByPriceRange(minPrice, maxPrice, evalPage, evalPageSize);
        }
        else {
            // Display all products
            products = productService.findAllPageable(evalPage, evalPageSize);

        }


        // Rest of the logic remains the same as before
        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);
        List<HeaderCategory> headers = headerCategoryRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages", products.getTotalPages());
        modelAndView.addObject("selectedPageSize", pageSize.orElse(INITIAL_PAGE_SIZE));
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("headers", headers);
        modelAndView.addObject("categories", categories);


        return modelAndView;
    }
    @GetMapping("/category/{id}")
    public List<Category> categoriesById(@PathVariable Integer id) {
       return categoryService.findByHeaderId(id);
    }


    @GetMapping("products")
    @ResponseBody
    public List<Product> product ()
    {
        return productService.findAllProducts();
    }






    //
    @GetMapping("/search")
    public ModelAndView showProductsByKeyword(@RequestParam("pageSize") Optional<Integer> pageSize,
                                              @RequestParam("page") Optional<Integer> page,
                                              @RequestParam(name = "name", defaultValue = "") String name) {
        var modelAndView = new ModelAndView("shop");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        Page<Product> products = productService.searchProductByKeyword(evalPage, evalPageSize, name);

        if (evalPage >= products.getTotalPages()) {
            return new ModelAndView("redirect:/search/?pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
        }

        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages", products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;
    }
    @GetMapping("/lower-price")
    public ModelAndView filterByLowerPrice(@RequestParam("pageSize") Optional<Integer> pageSize,
                                           @RequestParam("page") Optional<Integer> page) {
        var modelAndView = new ModelAndView("shop");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        Page<Product> products = productService.filterByLowerPrice(evalPage, evalPageSize);

        if (evalPage >= products.getTotalPages()) {
            return new ModelAndView("redirect:/lower-price/?pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
        }

        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages", products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;
    }
    @GetMapping("/higherPrice")
    public ModelAndView filterByHigherPrice(@RequestParam("pageSize") Optional<Integer> pageSize,
                                            @RequestParam("page") Optional<Integer> page) {
        var modelAndView = new ModelAndView("shop");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);
        String sort = "higher-price";
        Page<Product> products = productService.filterByHigherPrice(evalPage, evalPageSize,sort);

        if (evalPage >= products.getTotalPages()) {
            return new ModelAndView("redirect:/higherPrice/?pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
        }

        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages", products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;
    }



    @GetMapping("/find-products/{categoryId}")
    public ModelAndView viewProductByCategory(@PathVariable Integer categoryId,
                                              @RequestParam("pageSize") Optional<Integer> pageSize,
                                              @RequestParam("page") Optional<Integer> page) {

        var modelAndView = new ModelAndView("categories");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        Page<Product> products = productService.findProductsByCategory(evalPage, evalPageSize, categoryId);

        if (evalPage >= products.getTotalPages() && products.getTotalPages() > 0) {
            return new ModelAndView("redirect:/product/find-products/" + categoryId + "?pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
        }

        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);
        List<HeaderCategory> headerCategoryList = headerCategoryRepository.findAll();
        List<Category> categories = categoryService.listCategories();

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages", products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("categoryId", categoryId);
        modelAndView.addObject("categories",categories);
        return modelAndView;
    }


    @PostMapping("/add")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes redirectAttributes) {
        try {
            productService.save(imageProduct,product);
            redirectAttributes.addFlashAttribute("success","Add new product successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("error","Failed to add new product!");
        }
        return "redirect:/admin/view";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteProduct(@RequestParam("id") Long id) {
        productService.deleteById(id);
        return "redirect:/admin/view";
    }
    @GetMapping("/random/category")
    @ResponseBody
    public List<Product> getRandomAmountOfProducts() {
        return productService.getRandomAmountOfProducts();
    }


    @GetMapping("/shop-details/{productId}")
    public String getProductDetails(@PathVariable Long productId, Model model) {

        Product product = productService.getProductById(productId);

        model.addAttribute("product", product);

        return "shop-details";
    }


}
