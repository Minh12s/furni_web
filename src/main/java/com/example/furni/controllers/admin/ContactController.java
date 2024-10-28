package com.example.furni.controllers.admin;

import com.example.furni.entity.Brand;
import com.example.furni.entity.Contact;
import com.example.furni.service.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String getAllContacts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Page<Contact> contactsPage = contactService.getAllContacts(page, size);
        model.addAttribute("contactsPage", contactsPage);
        model.addAttribute("currentPage", page);
        return "admin/contact";
    }
}
