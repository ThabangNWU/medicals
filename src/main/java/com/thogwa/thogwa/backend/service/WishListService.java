//package com.thogwa.thogwa.backend.service;
//
//import com.thogwa.thogwa.backend.dto.ProductDto;
//import com.thogwa.thogwa.backend.model.WishList;
//import com.thogwa.thogwa.backend.repository.WishListRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class WishListService {
//
//    @Autowired
//    WishListRepository wishListRepository;
//
//    @Autowired
//    ProductService productService;
//
//    public void createWishlist(WishList wishList) {
//        wishListRepository.save(wishList);
//    }
//
//    public List<ProductDto> getWishListForUser(User user) {
//        final List<WishList> wishLists = wishListRepository.findAllByUserOrderByCreatedDateDesc(user);
//        List<ProductDto> productDtos = new ArrayList<>();
//        for (WishList wishList: wishLists) {
//            productDtos.add(productService.getProductDto(wishList.getProduct()));
//        }
//
//        return productDtos;
//    }
//}
