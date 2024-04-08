package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.controller.CustomerController;
import com.thogwa.thogwa.backend.dto.ProductDto;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Order;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.repository.CategoryRepository;
import com.thogwa.thogwa.backend.repository.OrderRepository;
import com.thogwa.thogwa.backend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
     @Autowired
     StorageService storageService;
     @Autowired
     CategoryRepository categoryRepository;

     @Autowired
    OrderRepository orderRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    public Product save(MultipartFile file, Product product) {
        Product product1 = new Product();
        try {
            if(file == null) {
                product1.setImageURL(null);
            } else {
                product1.setImageURL(storageService.storeFile(file));
            }
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
            product1.setPrice(product.getPrice());
            product1.setQuantity(product.getQuantity());
            product1.setHeaderCategory(product.getHeaderCategory());
            product1.setHeaderid(product.getHeaderid());
            product1.setCategoryid(product.getCategoryid());
            product1.setCategory(product.getCategory());
          return productRepository.save(product1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Product update(MultipartFile file, Product product,Long id) {

        try {
            Product product1 = productRepository.findById(id).get();
            if(file == null) {
                product1.setImageURL(null);
            } else {
                product1.setImageURL(storageService.storeFile(file));
            }

            if(product.getName() != null &&
            !(product.getName().equals(product1.getName()))) {
                product1.setName(product.getName());
            }
            if(product.getDescription() != null &&
                    !(product.getDescription().equals(product1.getDescription()))) {
                product1.setDescription(product.getDescription());
            }
            if(product.getPrice() != null &&
                    !(product.getPrice().equals(product1.getPrice()))) {
                product1.setPrice(product.getPrice());
            }
            if(product.getQuantity() != null &&
                    !(product.getQuantity().equals(product1.getQuantity()))) {
                product1.setQuantity(product.getQuantity());
            }
            if(product.getHeaderid() != null &&
                    !(product.getHeaderid().equals(product1.getHeaderid()))) {
                product1.setHeaderCategory(product.getHeaderCategory());
                product1.setHeaderid(product.getHeaderid());
            }
            if(product.getCategoryid() != null &&
                    !(product.getCategoryid().equals(product1.getCategoryid()))) {
                product1.setCategoryid(product.getCategoryid());
            }
            if(product.getCategoryid() != null &&
                    !(product.getCategoryid().equals(product1.getCategoryid()))) {
                product1.setCategoryid(product.getCategoryid());
                product1.setCategory(product.getCategory());
            }
            return productRepository.save(product1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public Product getProductById(Long productId) {
        // Assuming productRepository.findById returns an Optional<Product>
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
    }
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }


    public Page<Product> findAllPageable(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return productRepository.findAll(pageable);
    }
    public Page<Product> findAllPageableSort(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    public Page<Product> findByPriceRange(double minPrice, double maxPrice, int page, int pageSize) {
        // Assuming you have a method in your repository to find products by price range
        // Make sure to implement this method in your ProductRepository interface
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }
    public Page<Product> findAllPageableByAdmin(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return productRepository.findAll(pageable);
    }
    public Page<Product> searchProductByKeyword(Integer pageNo, Integer pageSize, String category) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return productRepository.findByCategory(category,pageable);
    }
    public Page<Product> findProductsByCategory(Integer pageNo, Integer pageSize, Integer categoryId) {
       Pageable pageable = PageRequest.of(pageNo,pageSize);
       return productRepository.getProductByCategoryId(categoryId,pageable);
    }
    public Page<Product> filterByLowerPrice(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return  productRepository.filterLowerProducts(pageable);
    }
    public Page<Product> findAll(Integer pageNo, Integer pageSize,double minPrice, double maxPrice) {

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return  productRepository.filterLowerProducts(pageable);
    }
    public Page<Product> filterByHigherPrice(Integer pageNo, Integer pageSize,String sort) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return  productRepository.filterHigherProducts(pageable);
    }




    public List<Product> getRandomAmountOfProducts() {
        List<Product> productList = productRepository.findAllByCategoryId(1);
        if (productList.isEmpty()) {
            throw new RuntimeException("Couldn't find any product in DB");
        }
        Collections.shuffle(productList);
        int randomSeriesLength = 8;
        int productListSize = productList.size();
        if (productListSize < randomSeriesLength) {
            randomSeriesLength = productListSize;
        }
        return productList.subList(0, randomSeriesLength);
    }

    public List<Product> getRandomFiveProduct() {
        List<Product> productList = productRepository.findAll();
        if (productList.isEmpty()) {
            throw new RuntimeException("Couldn't find any product in DB");
        }
        Collections.shuffle(productList);
        int randomSeriesLength = 5;
        return productList.subList(0, randomSeriesLength);
    }

    public List<Product> displayFiveDiscountProduct() {
        List<Product> products = productRepository.findAll();
        List<Product> filterProducts = new ArrayList<>();

        for (Product product : products){
            if(product.getSpecialPrice() == null && product.getSpecialPrice() == null) {
                filterProducts.add(product);
            }
        }
        if(filterProducts.size() >= 5) {
            Collections.shuffle(filterProducts);
        }

        return filterProducts.subList(0, 5);
    }
//    public List<Product> topRated() {
//        List<Product> products = productRepository.findAll();
//        List<Product> filteredProducts = new ArrayList<>();
//        if(products.size() > 8){
//            for(Product product : products) {
//                if(product.getTotalRating() != null) {
//                    filteredProducts.add(product);
//                }
//            }
//            Collections.shuffle(filteredProducts);
//            int randomSeries = 8;
//            return filteredProducts.subList(0,randomSeries);
//        } else {
//
//            if (products.isEmpty()) {
//                throw new RuntimeException("Couldn't find any product in DB");
//            }
//            Collections.shuffle(products);
//            int randomSeriesLength = 8;
//            return products.subList(0, randomSeriesLength);
//        }
//
//    }
    public List<Product> BestSeller() {

        List<Order>  findAllOrders  = orderRepository.findAll();
            List<Product> productList = productRepository.findAll();
            if (productList.isEmpty()) {
                throw new RuntimeException("Couldn't find any product in DB");
            }
            Collections.shuffle(productList);
            int randomSeriesLength = 10;
            return productList.subList(0, randomSeriesLength);
    }

    public List<Product> topRated() {

        List<Order>  findAllOrders  = orderRepository.findAll();
            List<Product> productList = productRepository.findAll();
            if (productList.isEmpty()) {
                throw new RuntimeException("Couldn't find any product in DB");
            }
            Collections.shuffle(productList);
            int randomSeriesLength = 10;
            return productList.subList(0, randomSeriesLength);
    }

    public List<Product> popularProduct() {

        List<Order>  findAllOrders  = orderRepository.findAll();

            List<Product> productList = productRepository.findAll();
            if (productList.isEmpty()) {
                throw new RuntimeException("Couldn't find any product in DB");
            }
            Collections.shuffle(productList);
            int randomSeriesLength = 10;
            return productList.subList(0, randomSeriesLength);

    }

    public Product addDiscount(BigDecimal specialPrice, Long id) {
        // Validate parameters
        if (specialPrice == null || specialPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Special price must be a positive value");
        }

        Product normalProductPrice = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));

        // Calculate off percentage
        BigDecimal offPercentage;
        if (normalProductPrice.getPrice().compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal percentage = specialPrice
                    .divide(normalProductPrice.getPrice(), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            offPercentage = BigDecimal.valueOf(100).subtract(percentage);
        } else {
            offPercentage = BigDecimal.ZERO; // Avoid division by zero, if price is zero
        }

        // Update product with discount information
        normalProductPrice.setSpecialPrice(specialPrice);
        normalProductPrice.setOffPercentage(offPercentage);

        LOGGER.info("Off percentage: " + offPercentage.toString());

        return productRepository.save(normalProductPrice);
    }



}
