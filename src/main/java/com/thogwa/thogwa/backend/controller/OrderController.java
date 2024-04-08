package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.*;
import com.thogwa.thogwa.backend.repository.AddressRepository;
import com.thogwa.thogwa.backend.repository.CartRepository;
import com.thogwa.thogwa.backend.repository.CustomerRepository;
import com.thogwa.thogwa.backend.repository.OrderRepository;
import com.thogwa.thogwa.backend.service.CustomerService;
import com.thogwa.thogwa.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrderController {
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
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @PostMapping("/add-to-order")
    public String addToOrder (@RequestBody List<Cart> useCart, Principal principal) {

        if(principal == null) {
            return "redirect:/login";
        }
        try {
            String username = principal.getName();
            orderService.saveOrderItems(useCart,username);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/";

    }
//    @GetMapping("/thogwa-order")
//    public String allOrderByUser(Principal principal, Model model) {
//        if(principal == null) {
//            return "redirect:/login";
//        }
//        String userName = principal.getName();
//        List<Order> userOrders = orderService.allOrderByUser(userName);
//        model.addAttribute("order", userOrders);
//        return "Order";
//
//    }
@GetMapping("/thogwa-order")
public ModelAndView allOrdersByUser(@RequestParam("pageSize") Optional<Integer> pageSize,
                                    @RequestParam("page") Optional<Integer> page,
                                    Principal principal) {
    var modelAndView = new ModelAndView("Order");
    Page<Order> orders;
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = page.filter(p -> p >= 1)
            .map(p -> p - 1)
            .orElse(INITIAL_PAGE);
    Address address = new Address();
    String email = principal.getName();
    User userAddressShipment = customerService.findByUsername(email);

    Address shipmentAdress = addressRepository.findByCustomers(userAddressShipment);


    orders = orderService.allOrdersByUser(evalPage, evalPageSize, principal.getName());

    var pager = new Pager(orders.getTotalPages(), orders.getNumber(), BUTTONS_TO_SHOW);

    modelAndView.addObject("orders", orders); // Changed from "order" to "orders"
    modelAndView.addObject("totalItems", orders.getTotalElements());
    modelAndView.addObject("totalPages", orders.getTotalPages());
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    modelAndView.addObject("address",shipmentAdress);

    return modelAndView;
}

    @GetMapping("/thogwa-order/delivery")
    public ModelAndView allOrdersByUserDelivery(@RequestParam("pageSize") Optional<Integer> pageSize,
                                                @RequestParam("page") Optional<Integer> page,
                                                Principal principal) {
        ModelAndView modelAndView = new ModelAndView("Order");
        Page<Order> orders;
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        orders = orderService.allOrdersByUser(evalPage, evalPageSize, principal.getName());

        // Filtering out orders with null delivery dates
        List<Order> filteredOrders = orders.getContent()
                .stream()
                .filter(order -> order.getDeliveryDate() != null)
                .collect(Collectors.toList());

        Pager pager = new Pager(orders.getTotalPages(), orders.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("orders", filteredOrders);
        modelAndView.addObject("totalItems", orders.getTotalElements());
        modelAndView.addObject("totalPages", orders.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteProduct(@RequestParam("id") Long id) {
        orderService.cancelOrder(id);
        return "redirect:/thogwa-order";
    }

    @GetMapping("/admin/orders")
    public ModelAndView getAll(@RequestParam("pageSize") Optional<Integer> pageSize,
                               @RequestParam("page") Optional<Integer> page,
                               Principal principal) {
        var modelAndView = new ModelAndView("admin/orders");
        Page<Order> orders;
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        orders = orderService.findAllPageable(evalPage, evalPageSize);


        if (evalPage >= orders.getTotalPages()) {
            // Redirect to the last available page
            return new ModelAndView("redirect:/product/?pageSize=" + evalPageSize + "&page=" + (orders.getTotalPages() - 1));
        }

        // Retrieve shipping address for each order
        List<Address> shippingAddresses = new ArrayList<>();
        for (Order order : orders.getContent()) {
            User user = order.getUser();
            Address shippingAddress = addressRepository.findByCustomers(user);

            shippingAddresses.add(shippingAddress);
        }

        var pager = new Pager(orders.getTotalPages(), orders.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("orders", orders); // Changed from "order" to "orders"
        modelAndView.addObject("totalItems", orders.getTotalElements());
        modelAndView.addObject("totalPages", orders.getTotalPages());
        modelAndView.addObject("address",shippingAddresses);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("shippingAddresses", shippingAddresses);

        return modelAndView;
    }


    @GetMapping("/admin/ordersByUser")
    public ModelAndView ordersByUser(@RequestParam("pageSize") Optional<Integer> pageSize,
                                     @RequestParam("page") Optional<Integer> page,
                                     @RequestParam(name = "deliveryDate", required = false) String deliveryDate,
                                     Principal principal, @PathVariable Long userId) {
        var modelAndView = new ModelAndView("admin/orders");
        Page<Order> orders;
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        if (deliveryDate != null && deliveryDate.equals("null")) {
            // Handle filtering by deliveryDate being null
            orders = orderService.ordersByUserWithNullDeliveryDate(evalPage, evalPageSize, userId);
        } else if (deliveryDate != null && !deliveryDate.isEmpty()) {
            // Handle filtering by specific delivery date
            orders = orderService.ordersByUserWithDeliveryDate(evalPage, evalPageSize, userId, deliveryDate);
        } else {
            // Handle other filters or default case
            orders = orderService.ordersByUser(evalPage, evalPageSize, userId);
        }

        if (evalPage >= orders.getTotalPages()) {
            // Redirect to the last available page
            return new ModelAndView("redirect:/product/?pageSize=" + evalPageSize + "&page=" + (orders.getTotalPages() - 1));
        }

        var pager = new Pager(orders.getTotalPages(), orders.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("orders", orders); // Changed from "order" to "orders"
        modelAndView.addObject("totalItems", orders.getTotalElements());
        modelAndView.addObject("totalPages", orders.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;

    }


    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Long id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Order Accepted");
            return "redirect:/admin/orders";
        }
    }

    @RequestMapping(value = "/admin/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.cancelOrder(id);
            return "redirect:/admin/orders";
        }
    }

}
