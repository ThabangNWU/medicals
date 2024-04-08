package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
@Controller
@RequestMapping("")
public class homePage {
    @Autowired
    private ProductService productService;


    @GetMapping("")
     public  ModelAndView getRandomAmountOfProducts() {
        var modelAndView  = new ModelAndView("index");
        List<Product> randomCategoryProducts = productService.getRandomAmountOfProducts();
        List<Product> randomFiveCategory = productService.getRandomFiveProduct();
        List<Product> bestSeller = productService.BestSeller();
        List<Product> popularProduct = productService.popularProduct();
        List<Product> topRated = productService.topRated();
        List<Product> discounts = productService.displayFiveDiscountProduct();

        modelAndView.addObject("categoryRandom", randomCategoryProducts);
        modelAndView.addObject("bestSeller", bestSeller);
        modelAndView.addObject("categoryFiveProduct", randomFiveCategory);
        modelAndView.addObject("popularProduct",popularProduct);
        modelAndView.addObject("topRated", topRated);
        modelAndView.addObject("discounts", discounts);

        return modelAndView;
    }
}
