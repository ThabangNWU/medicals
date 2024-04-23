package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.Address;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.AddressRepository;
import com.thogwa.thogwa.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class Checkout {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AddressRepository addressRepository;
    @GetMapping("/checkout")
    public String checkout (Principal principal, Model model) {
        String userName = principal.getName();
        User user = customerRepository.findByUsername(userName);
      
        model.addAttribute("email", user.getUsername());
        model.addAttribute("customer",user.getFirstName() + " " + user.getLastName());
        model.addAttribute("cellPhone",user.getMobileNumber());
        return "checkout";
    }


}
