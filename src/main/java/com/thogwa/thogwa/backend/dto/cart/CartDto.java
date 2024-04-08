package com.thogwa.thogwa.backend.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public class CartDto {
    private List<CartItemDto> cartItems;
    private BigDecimal totalCost;

    public CartDto() {
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }

    public  BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost( BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
