package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.Cart;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.CartRepository;
import com.thogwa.thogwa.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartRepository cartRepository;

    public void saveCartItems(List<Cart> cartItems, String username) {

        User user = customerService.findByUsername(username);

        List<Cart> cartList = new ArrayList<>();


        for (int i = 0; i < cartItems.size(); i++) {
            Cart cart = new Cart();  // Create a new instance for each iteration

            cart.setProductName(cartItems.get(i).getProductName());
            cart.setProductImageName(cartItems.get(i).getProductImageName());
            cart.setProduct(cartItems.get(i).getProduct());
            cart.setProductID(cartItems.get(i).getProductID());
            cart.setUser(user);
            cart.setQuantity(cartItems.get(i).getQuantity());
            cartList.add(cart);
        }

        cartRepository.saveAll(cartList);
    }

}
