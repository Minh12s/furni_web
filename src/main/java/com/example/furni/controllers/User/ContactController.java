package com.example.furni.controllers.User;

import com.example.furni.entity.Contact;
import com.example.furni.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Hiển thị form contact
    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact()); // Thêm đối tượng Contact vào model
        return "User/contact"; // Trả về trang contact
    }

    // Xử lý form contact sau khi người dùng gửi
    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute("contact") Contact contact, Model model) {
        // Gọi service để xử lý lưu contact vào database
        contactService.saveContact(contact);

        // Sau khi xử lý thành công, thêm thông báo vào model
        model.addAttribute("successMessage", "Your message has been sent successfully!");

        // Trả về trang contact với thông báo thành công
        return "User/contact";
    }
}
