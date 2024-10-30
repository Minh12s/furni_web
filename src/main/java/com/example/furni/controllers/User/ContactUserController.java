package com.example.furni.controllers.User;

import com.example.furni.entity.Contact;
import com.example.furni.service.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ContactUserController extends BaseController{

    @Autowired
    private ContactService contactService;


    // Hiển thị form contact
    @GetMapping("/contact")
    public String showContactForm(Model model, HttpSession session) {
        model.addAttribute("contact", new Contact());
        String contactSuccessMessage = (String) session.getAttribute("contactSuccess");
        if (contactSuccessMessage != null) {
            model.addAttribute("success", contactSuccessMessage);
            session.removeAttribute("contactSuccess");
        }
        return "User/contact";
    }

    // Xử lý form contact sau khi người dùng gửi
    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute("contact") Contact contact, HttpSession session) {
        contact.setStatus("pending");
        contactService.saveContact(contact);
        session.setAttribute("contactSuccess", "Your message has been sent successfully!");
        return "redirect:/contact";
    }

}
