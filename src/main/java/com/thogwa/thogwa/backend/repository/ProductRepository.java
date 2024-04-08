package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    Page<Product> findByCategory(String productName, Pageable pageable);
    List<Product> findAllByCategoryId(Integer category);
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> getProductByCategoryId(Integer categoryId, Pageable pageable);

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.imageurl, p.price, p.categoryid,p.headerid, p.total_rating, p.quantity, p.rating_stars, p.special_price " +
            "from products p order by p.price asc ", nativeQuery = true)
    Page<Product> filterLowerProducts(Pageable pageable);

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.imageurl, p.price, p.categoryid,p.headerid, p.total_rating, p.quantity, p.rating_stars, p.special_price " +
            "from products p order by p.price desc ", nativeQuery = true)
    Page<Product> filterHigherProducts(Pageable pageable);

    Product findByName(String name);

    Page<Product> findAll( Pageable pageable);
    Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

}