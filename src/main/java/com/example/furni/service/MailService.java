package com.example.furni.service;

import com.example.furni.entity.Cart;
import com.example.furni.entity.Orders;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendThankYouEmail(String fullName, String fixedEmail, String toEmail, String formattedOrderDate, Orders order, List<Cart> cartItems, double subtotal, double tax, double shippingFee, double totalAmount) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Đặt địa chỉ gửi và tiêu đề
        helper.setTo(fixedEmail);  // Gửi đến email của khách hàng
        helper.setSubject("Thank You for Your Order!");

        // Tạo nội dung email
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("order", order);
        context.setVariable("cartItems", cartItems);
        context.setVariable("subtotal", subtotal);
        context.setVariable("tax", tax);
        context.setVariable("shippingFee", shippingFee);
        context.setVariable("totalAmount", totalAmount);

        // Thêm ngày tháng đã định dạng vào context
        context.setVariable("orderDate", formattedOrderDate);

        // Giả sử bạn có một mẫu email HTML để hiển thị thông tin
        String htmlContent = templateEngine.process("email/mailthankyou", context);

        helper.setText(htmlContent, true); // Gửi nội dung HTML

        // Gửi email
        mailSender.send(message);
    }


}
