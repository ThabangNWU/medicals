package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.Order;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findAllByUserUsername(String username, Pageable pageable);
    Page<Order> findAllByUser(String username, Pageable pageable);
    Page<Order> findAllOrderByUserId(Long id, Pageable pageable);
    Page<Order> findAll( Pageable pageable);

    Order findByProductName(String name);
    Page<Order> findAllByUserIdAndDeliveryDateIsNull(Long userId, Pageable pageable);

    Page<Order> findAllByUserIdAndDeliveryDate(Long userId, String deliveryDate, Pageable pageable);
//    Page<Order> findByOrderDateBetweenDay(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
//    Page<Order> findByOrderDateBetweenMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth, Pageable pageable);
    Page<Order> findByDeliveryDateIsNull(Pageable pageable);
    Page<Order> findByDeliveryDateIsNotNull(Pageable pageable);


}


