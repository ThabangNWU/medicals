package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.common.ApiResponse;
import com.thogwa.thogwa.backend.model.Cart;
import com.thogwa.thogwa.backend.repository.CustomerRepository;
import com.thogwa.thogwa.backend.service.CartService;
import com.thogwa.thogwa.backend.service.CategoryService;
import com.thogwa.thogwa.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/add-to-cart")
    public String addToCart (@RequestBody List<Cart> useCart, Principal principal) {

        if(principal == null) {
            return "redirect:/login";
        }
        try {
            String username = principal.getName();
            cartService.saveCartItems(useCart,username);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/checkout";
    }

}
