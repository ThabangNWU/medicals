package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.Address;
import com.thogwa.thogwa.backend.repository.AddressRepository;
import com.thogwa.thogwa.backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressRepository addressRepository;
    @PostMapping("/add-address")
    public String addAddress(Address address, Principal principal, Model model) {

        String username = principal.getName();
        addressService.addShipmentAddress(address, username);
        return "redirect:/checkout"; // Redirect to a success page
    }
}
