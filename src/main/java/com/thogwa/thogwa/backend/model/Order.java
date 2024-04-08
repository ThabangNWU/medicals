package com.thogwa.thogwa.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date orderDate;
    private Date deliveryDate;
    private String orderStatus;
    private boolean isAccept;
    private BigDecimal totalPrice;
    private String productID;
    private String productName;
    private String productImageName;
    private BigDecimal productPrice;
    private Integer quantity;
    private String paymentMethod;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToOne
    @JoinColumn(name = "shipment")
    private Address shipmentAddress;
}
