package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.dto.dto.CustomerDto;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    @PostMapping("/registration")
    public String saveCustomer(@ModelAttribute("user") User user, Model model) {
        customerService.save(user);
        model.addAttribute("message","Registered Successful");
        return "pages-register";
    }
    @GetMapping("/login")
    public String login() {
       return "login";
    }
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/user/addNew")
    public RedirectView addNew(User user, RedirectAttributes redirectAttributes) {
        customerService.save(user);
        RedirectView redirectView = new RedirectView("/login",true);
        redirectAttributes.addFlashAttribute("message", "You successfully registered! You can now login");
        return redirectView;
    }

    //Get All Users
    @GetMapping("users")
    public String findAll(Model model){
        model.addAttribute("users", customerService.findAll());
        return "user";
    }
    @PostMapping("/role/thogwa-admin")
    public void saveRole(Principal principal,@RequestParam(name = "role") String role) {
        customerService.saveRole(principal.getName(), role);
    }

    @RequestMapping("users/findById")
    @ResponseBody
    public User findById(long id)
    {
        return customerService.findById(id);
    }

    @RequestMapping(value="users/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public String update(User user) {
        customerService.save(user);
        return "redirect:/users";
    }

    @RequestMapping(value="users/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String delete(long id) {
        customerService.delete(id);
        return "redirect:/users";
    }

}
