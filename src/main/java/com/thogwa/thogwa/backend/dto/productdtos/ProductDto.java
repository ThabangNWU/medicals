package com.thogwa.thogwa.backend.dto.productdtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.HeaderCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductDto {


    private Integer productId;
    private  String name;
    private String imageURL;
    private  BigDecimal price;
    private String description;
    private BigDecimal specialPrice;
    private Double offPercentage;
    private Integer quantity;

    private Integer totalRating;
    private Double ratingStars;

    private Category category;


    private HeaderCategory headerCategory;

}
