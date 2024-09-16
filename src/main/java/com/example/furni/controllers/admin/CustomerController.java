package com.example.furni.controllers.admin;

import com.example.furni.entity.OrderProduct;
import com.example.furni.entity.Orders;
import com.example.furni.service.CustomerService;
import com.example.furni.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String getCustomers(Model model,
                               @RequestParam(defaultValue = "") String userNameSearch,
                               @RequestParam(defaultValue = "") String addressSearch,
                               @RequestParam(defaultValue = "") String phoneNumberSearch,
                               @RequestParam(defaultValue = "") String emailSearch,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        // Lấy danh sách khách hàng đã lọc
        Page<User> customerPage = customerService.getFilteredCustomers(userNameSearch, addressSearch, phoneNumberSearch, emailSearch, page, size);

        // Đưa danh sách khách hàng vào model để hiển thị
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("size", size);
        return "admin/Customer/customer";
    }

    @GetMapping("/orderuser")
    public String getOrderUser(@RequestParam("userId") int userId, Model model) {
        // Lấy danh sách đơn hàng của user
        List<Orders> orders = customerService.getOrdersByUserId(userId);

        // Đưa danh sách đơn hàng vào model để hiển thị
        model.addAttribute("orders", orders);
        return "admin/Customer/OrderUser";
    }

    @GetMapping("/orderdetailuser/{id}")
    public String getOrderDetailsUser(@PathVariable("id") int id, Model model) {
        Orders order = customerService.getOrderById(id);
        if (order != null) {
            List<OrderProduct> orderProducts = customerService.getOrderProductsByOrderId(id);
            model.addAttribute("order", order);
            model.addAttribute("orderProducts", orderProducts);
        }
        return "admin/Customer/OrderDetailUser";
    }
}
