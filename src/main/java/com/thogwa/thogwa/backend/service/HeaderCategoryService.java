package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.HeaderCategory;
import com.thogwa.thogwa.backend.repository.HeaderCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeaderCategoryService {
    private final HeaderCategoryRepository headerCategoryRepository;
    public HeaderCategoryService(HeaderCategoryRepository headerCategoryRepository) {
        this.headerCategoryRepository = headerCategoryRepository;
    }

    public void addHeaderCategory(HeaderCategory headerCategory) {
        headerCategoryRepository.save(headerCategory);
    }

    public List<HeaderCategory> findAll(){
        return headerCategoryRepository.findAll();
    }

    //Get HeaderCategory By Id
    public Optional<HeaderCategory> findById(int id) {
        return headerCategoryRepository.findById(id);
    }

    //Delete HeaderCategory
    public void delete(int id) {
        headerCategoryRepository.deleteById(id);
    }

}
