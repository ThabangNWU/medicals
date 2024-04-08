package com.thogwa.thogwa.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class YourController {

    @GetMapping("/latest-News")
    public String showLatestNews() {
        return "latest-News";
    }
    @GetMapping("/contact")
    public String showContact() {
        return "contact";
    }
    @GetMapping("/about-us")
    public String showAboutUs() {
        return "about-us";
    }
    @GetMapping("/shop")
    public String showShop() {
        return "shop";
    }
    @GetMapping("/blog")
    public String showBlog() {
        return "blog";
    }
    @GetMapping("/thogwa-product")
    public String showProducts() {
        return "products";
    }

    @GetMapping("/payment/successful")
    public String showPayment()
    {
        return "payment-successful";
    }
    @GetMapping("/payment/failed")
    public String showPaymentFailed()
    {
        return "payment_failed";
    }


}
