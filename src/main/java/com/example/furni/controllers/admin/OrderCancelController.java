package com.example.furni.controllers.admin;

import com.example.furni.entity.Blog;
import com.example.furni.entity.OrderCancel;
import com.example.furni.service.OrderCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class OrderCancelController {

    @Autowired
    private OrderCancelService orderCancelService;

    @GetMapping("/orderCancel")
    public String orderCancel(Model model ,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Page<OrderCancel> ordersCancelPage = orderCancelService.getAllOrderCancels(page, size);
        Map<String, Long> reasonCounts = orderCancelService.getReasonCounts();

        // Biểu tượng cho từng lý do
        Map<String, String> reasonIcons = new HashMap<>();
        reasonIcons.put("Order takes a long time to confirm", "mdi-clock");
        reasonIcons.put("I don't need to buy anymore", "mdi-cancel");
        reasonIcons.put("I want to update my shipping address", "mdi-truck");
        reasonIcons.put("I found a better place to buy (Cheaper, more reputable, faster delivery...)", "mdi-thumb-up");
        reasonIcons.put("I don't have enough money to buy it", "mdi-currency-usd");

        // Tính số lượng lý do "Khác"
        long otherReasonsCount = reasonCounts.entrySet().stream()
                .filter(entry -> !reasonIcons.containsKey(entry.getKey()))
                .mapToLong(Map.Entry::getValue)
                .sum();

        if (otherReasonsCount > 0) {
            reasonCounts.put("Other", otherReasonsCount);
        }
        model.addAttribute("reasonCounts", reasonCounts);
        model.addAttribute("reasonIcons", reasonIcons);
        model.addAttribute("ordersCancelPage", ordersCancelPage);
        model.addAttribute("size", size);


        return "admin/OrderCancel/orderCancel";
    }

}
