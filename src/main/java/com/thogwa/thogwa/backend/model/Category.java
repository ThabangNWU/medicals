package com.thogwa.thogwa.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thogwa.thogwa.backend.repository.HeaderCategoryRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_name")
    private @NotBlank String categoryName;

    @ManyToOne
    @JoinColumn(name = "headerid",insertable = false,updatable = false)
    private HeaderCategory  headerCategory;
    private Integer headerid;

    @OneToMany(mappedBy = "category")
    private List<Product> Products;

}
