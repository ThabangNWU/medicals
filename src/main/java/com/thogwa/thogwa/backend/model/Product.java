package com.thogwa.thogwa.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private @NotNull String name;
    private @NotNull String imageURL;
    private @NotNull BigDecimal price;
    private @NotNull String description;
    private BigDecimal specialPrice;

    private BigDecimal offPercentage;
    private Integer quantity;

    private Integer totalRating;
    private Double ratingStars;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryid", insertable = false, updatable = false)
    private Category category;
    private Integer categoryid;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "headerid", insertable = false, updatable = false)
    private HeaderCategory headerCategory;
    private Integer headerid;


}
