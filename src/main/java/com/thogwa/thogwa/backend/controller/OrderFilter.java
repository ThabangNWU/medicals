package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.Order;
import com.thogwa.thogwa.backend.model.Pager;
import com.thogwa.thogwa.backend.repository.CartRepository;
import com.thogwa.thogwa.backend.repository.OrderRepository;
import com.thogwa.thogwa.backend.service.CustomerService;
import com.thogwa.thogwa.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

public class OrderFilter {
    @Autowired
    private OrderService orderService;
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 8;
    private static final int[] PAGE_SIZES = {8, 12, 16, 18, 20};
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @GetMapping("/admin/orders/filter")
    public ModelAndView filterOrders(@RequestParam("filterType") String filterType,
                                     @RequestParam("pageSize") Optional<Integer> pageSize,
                                     @RequestParam("page") Optional<Integer> page,
                                     Principal principal) {
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        Page<Order> filteredOrders;

        // Call appropriate method in orderService based on filterType
        switch (filterType) {
//            case "today":
//                filteredOrders = orderService.findTodayOrders(evalPage, evalPageSize);
//                break;
//            case "thisMonth":
//                filteredOrders = orderService.findThisMonthOrders(evalPage, evalPageSize);
//                break;
            case "asc":
                filteredOrders = orderService.findOrdersInAscendingOrder(evalPage, evalPageSize);
                break;
            case "desc":
                filteredOrders = orderService.findOrdersInDescendingOrder(evalPage, evalPageSize);
                break;
            case "nullDeliveryDate":
                filteredOrders = orderService.findOrdersWithNullDeliveryDate(evalPage, evalPageSize);
                break;
            case "hasDeliveryDate":
                filteredOrders = orderService.findOrdersWithDeliveryDate(evalPage, evalPageSize);
                break;
            default:
                // Handle invalid filterType, maybe redirect to default view
                return new ModelAndView("redirect:/admin/orders");
        }

        ModelAndView modelAndView = new ModelAndView("admin/orders");
        var pager = new Pager(filteredOrders.getTotalPages(), filteredOrders.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("orders", filteredOrders);
        modelAndView.addObject("totalItems", filteredOrders.getTotalElements());
        modelAndView.addObject("totalPages", filteredOrders.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;
    }

}
