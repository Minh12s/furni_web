package com.example.furni.controllers.admin;

import com.example.furni.entity.Material;
import com.example.furni.entity.OrderReturn;
import com.example.furni.entity.Review;
import com.example.furni.service.OrderReturnService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.mail.javamail.JavaMailSender;
@Controller
@RequestMapping("/admin")
public class OrderReturnController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private OrderReturnService orderReturnService;
    @GetMapping("/orderReturn")
    public String OrderReturn(Model model,HttpSession session,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) Double refundAmount,
                              @RequestParam(required = false) String search){
        Page<OrderReturn> orderReturnPage = orderReturnService.filterOrderReturns(page, size, status, refundAmount, search);
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
            // Gọi service để cập nhật trạng thái và hồi lại qty nếu cần
            orderReturnService.updateOrderReturnStatus(orderReturnId, status);

            session.setAttribute("successMessage", "Order return status updated successfully.");

            // Nếu trạng thái là "rejected", chuyển hướng tới trang rejectReason
            if ("rejected".equalsIgnoreCase(status)) {
                return "redirect:/admin/rejectReason";
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Unable to update Order return status.");
        }

        return "redirect:/admin/orderReturn";
    }

    @GetMapping("/rejectReason")
    public String RejectReason(){
        return "admin/OrderReturn/rejectReason";
    }
    @PostMapping("/rejectReason")
    public String handleRejectReason(@RequestParam("rejectreason") String rejectReason, @RequestParam("orderId") Long orderId, Model model) {
        // Gửi email
        try {
            sendRejectionEmail(rejectReason);
            model.addAttribute("successMessage", "Rejection email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to send rejection email.");
        }
        return "redirect:/admin/orderReturn";
    }

    private void sendRejectionEmail(String rejectReason) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(""); // mail người gửi
        helper.setTo(""); // mail người nhận
        helper.setSubject("Order Rejection Confirmation");

        // Tạo nội dung HTML cho email, chèn lý do từ rejectReason
        String emailContent = "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Order Rejection Confirmation</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f8f8f8;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #fff;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            margin-bottom: 20px;\n" +
                "            line-height: 1.6;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #FFA45C;\n" +
                "            color: #fff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Order Rejection Confirmation</h1>\n" +
                "        </div>\n" +
                "        <p>Dear Valued Customer,</p>\n" +
                "        <p>We regret to inform you that your refund request could not be processed successfully at this time.</p>\n" +
                "        <p>Reason for rejection: <strong>" + rejectReason + "</strong></p>\n" +
                "        <p>If you have any questions or need further assistance, please don't hesitate to reach out to us. We're always here to help.</p>\n" +
                "        <p>Thank you for choosing us, and we hope to serve you better in the future.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        // Đặt nội dung email vào body
        helper.setText(emailContent, true);  // true để gửi email dưới dạng HTML

        // Gửi email
        mailSender.send(message);
    }
}
