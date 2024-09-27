package com.example.furni.controllers.admin;

import com.example.furni.entity.Material;
import com.example.furni.entity.OrderReturn;
import com.example.furni.entity.Review;
import com.example.furni.service.OrderReturnService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class OrderReturnController {

    @Autowired
    private OrderReturnService orderReturnService;
    @GetMapping("/orderReturn")
    public String OrderReturn(Model model,HttpSession session,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size){
        Page<OrderReturn> orderReturnPage = orderReturnService.getOrderReturnPaginated(page, size);
        Map<String, Long> reasonCounts = orderReturnService.getReasonCounts();

        // Biểu tượng cho từng lý do
        Map<String, String> reasonIcons = new HashMap<>();
        reasonIcons.put("Missing quantity/accessories", "mdi-timer-sand");
        reasonIcons.put("Seller sent wrong item", "mdi-cancel");
        reasonIcons.put("The product cannot be used", "mdi-package-variant-closed");
        reasonIcons.put("Different from description", "mdi-thumb-up");
        reasonIcons.put("Counterfeit goods, imitation goods", "mdi-cash-refund");
        reasonIcons.put("The goods are intact but no longer needed", "mdi-arrow-down-bold");

        model.addAttribute("reasonCounts", reasonCounts);
        model.addAttribute("reasonIcons", reasonIcons);
        model.addAttribute("orderReturnPage", orderReturnPage);
        model.addAttribute("size", size);

        // Lấy thông báo thành công từ session và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }
        return "admin/OrderReturn/orderReturn";
    }
    @GetMapping("/orderReturnDetails/{id}")
    public String OrderReturnDetails(@PathVariable("id") int id, Model model){
        OrderReturn orderReturn = orderReturnService.getOrderReturnById(id);
        if (orderReturn != null) {
            model.addAttribute("orderReturn", orderReturn);
        }
        return "admin/OrderReturn/orderReturnDetails";
    }
    @PostMapping("/updateOrderReturn/{orderReturnId}")
    public String updateOrderReturnStatus(@PathVariable("orderReturnId") int orderReturnId,
                                          @RequestParam("status") String status,
                                          RedirectAttributes redirectAttributes,
                                          HttpSession session) {
        try {
            // Gọi service để cập nhật trạng thái và hồi lại qty
            orderReturnService.updateOrderReturnStatus(orderReturnId, status);

            session.setAttribute("successMessage", "Order return status updated successfully.");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Unable to update Order return status.");
        }

        return "redirect:/admin/orderReturn";
    }
    @GetMapping("/rejectReason")
    public String RejectReason(){
        return "admin/OrderReturn/rejectReason";
    }
}
