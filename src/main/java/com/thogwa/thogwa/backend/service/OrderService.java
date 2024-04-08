package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.Cart;
import com.thogwa.thogwa.backend.model.Order;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.CartRepository;
import com.thogwa.thogwa.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    EmailSenderService emailSenderService;

    public void saveOrderItems(List<Cart> cartItems, String username) {

        User user = customerService.findByUsername(username);
        List<Order> orders = new ArrayList<>();
        List<Cart> cartOnDB = cartRepository.findAll();
        for (int i = 0; i < cartItems.size(); i++) {
            LocalDate currentDate = LocalDate.now();
            BigDecimal quantityBigDecimal = new BigDecimal(cartItems.get(i).getQuantity());
            Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Order order = new Order();
            order.setQuantity(cartItems.get(i).getQuantity());
            order.setProductImageName(cartItems.get(i).getProductImageName());
            order.setOrderDate(date);
            order.setProductPrice(cartItems.get(i).getProduct());
            order.setOrderStatus("Pending");
            order.setAccept(false);
            order.setUser(user);
            order.setProductName(cartItems.get(i).getProductName());
            order.setProductID(cartItems.get(i).getProductID());

            order.setTotalPrice(cartItems.get(i).getProduct().multiply(quantityBigDecimal));
            orders.add(order);
        }
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("admorelebele.com");
            mailMessage.setTo("kakapa018.gmail.com");
            mailMessage.setSubject("Orders !!!");
            mailMessage.setText(orders.toString());
            emailSenderService.sendEmail(mailMessage);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        orderRepository.saveAll(orders);

    }

    public List<Order> allOrderByUser (String userName) {
        List<Order> orderList = new ArrayList<>();
        List<Order>  allOrder = orderRepository.findAll();
        User user = customerService.findByUsername(userName);
        Order orderTemp = null;
        for(int i = 0 ; i < allOrder.size(); i++) {
            if(user.getUsername().equals(allOrder.get(i).getUser().getUsername())) {
                orderTemp = allOrder.get(i);
                orderList.add(orderTemp);
            }
        }
        return orderList;
    }
    public Page<Order> allOrdersByUser(Integer pageNo, Integer pageSize, String userName) {
        User user = customerService.findByUsername(userName);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAllByUserUsername(user.getUsername(), pageable);
    }

    public Page<Order> ordersByUser(Integer pageNo, Integer pageSize, Long userId) {
        User user = customerService.findById(userId);
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return orderRepository.findAllOrderByUserId(user.getId(),pageable);
    }
    public Page<Order> ordersByUserWithNullDeliveryDate(Integer pageNo, Integer pageSize, Long userId) {
        User user = customerService.findById(userId);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAllByUserIdAndDeliveryDateIsNull(user.getId(), pageable);
    }

    public Page<Order> ordersByUserWithDeliveryDate(Integer pageNo, Integer pageSize, Long userId, String deliveryDate) {
        User user = customerService.findById(userId);
        // Parse and handle deliveryDate parameter as needed
        // Example: You might parse it to a Date object or use it directly based on your implementation
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAllByUserIdAndDeliveryDate(user.getId(), deliveryDate, pageable);
    }

    public Page<Order> allOrderByUserDelivery(Integer pageNo, Integer pageSize,String userName) {
        User user = customerService.findByUsername(userName);
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return orderRepository.findAllByUser(user.getUsername(),pageable);
    }
    public Page<Order> findAllPageable(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return orderRepository.findAll(pageable);
    }
    public Order acceptOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setDeliveryDate(new Date());
        order.setAccept(true);
        order.setOrderStatus("delivered");
        return orderRepository.save(order);
    }
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }


//    public Page<Order> findTodayOrders(int pageNo, int pageSize) {
//        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();
//        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        return orderRepository.findByOrderDateBetween(startOfDay, endOfDay, pageable);
//    }


//    public Page<Order> findThisMonthOrders(int pageNo, int pageSize) {
//        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
//        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
//        LocalDateTime endOfMonth = firstDayOfMonth.plusMonths(1).atStartOfDay().minusSeconds(1);
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        return orderRepository.findByOrderDateBetween(startOfMonth, endOfMonth, pageable);
//    }


    public Page<Order> findOrdersInAscendingOrder(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orderDate").ascending());
        return orderRepository.findAll(pageable);
    }


    public Page<Order> findOrdersInDescendingOrder(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orderDate").descending());
        return orderRepository.findAll(pageable);
    }


    public Page<Order> findOrdersWithNullDeliveryDate(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findByDeliveryDateIsNull(pageable);
    }


    public Page<Order> findOrdersWithDeliveryDate(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findByDeliveryDateIsNotNull(pageable);
    }
}
