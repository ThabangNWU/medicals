package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.HeaderCategory;
import com.thogwa.thogwa.backend.service.HeaderCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class HeaderCategoryController {
    @Autowired private HeaderCategoryService headerCategoryService;
    //Get All HeaderCategories
    @GetMapping("header")
    public String findAll(Model model){
        model.addAttribute("header", headerCategoryService.findAll());
        return "admin/addHeader";
    }

    @RequestMapping("header/findById")
    @ResponseBody
    public Optional<HeaderCategory> findById(Integer id)
    {
        return headerCategoryService.findById(id);
    }

    //Add HeaderCategory
    @PostMapping("/header/addNew")
    public String addNew(@ModelAttribute("headerCategory") HeaderCategory headerCategory) {
        headerCategoryService.addHeaderCategory(headerCategory);
        return "redirect:/admin/addHeader";
    }
    @RequestMapping(value="header/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public String update(HeaderCategory headerCategory) {
        headerCategoryService.addHeaderCategory(headerCategory);
        return "redirect:/admin/addHeader";
    }

    @RequestMapping(value="header/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String delete(Integer id) {
        headerCategoryService.delete(id);
        return "redirect:/admin/addHeader";
    }
}
