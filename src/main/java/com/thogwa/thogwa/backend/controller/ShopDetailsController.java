package com.thogwa.thogwa.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopDetailsController {
    @GetMapping("/shop-details")
    public String showShopDetails() {
        return "shop-details";
    }
    @GetMapping("/blog-details")
    public String showBlogDetails() {
        return "blog-details";
    }
}
