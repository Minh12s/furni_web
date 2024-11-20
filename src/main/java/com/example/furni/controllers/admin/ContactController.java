package com.example.furni.controllers.admin;

import com.example.furni.entity.Contact;
import com.example.furni.service.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ContactController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String getAllContacts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model, HttpSession session) {
        Page<Contact> contactsPage = contactService.getAllContacts(page, size);
        model.addAttribute("contactsPage", contactsPage);
        model.addAttribute("currentPage", page);

        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }
        // Lấy thông báo lỗi từ session và xóa sau khi lấy
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }
        return "admin/Contact/contact";
    }

    // Sửa lại phương thức nhận ID bằng @PathVariable
    @GetMapping("/contactForm/{id}")
    public String showContactForm(@PathVariable("id") int id, Model model) {
        Optional<Contact> contactOpt = contactService.getContactById(id);
        if (contactOpt.isPresent()) {
            Contact contact = contactOpt.get();
            model.addAttribute("contact", contact);
        } else {
            model.addAttribute("errorMessage", "Contact not found.");
        }
        return "admin/Contact/contactForm"; // Đường dẫn tới view
    }

    @PostMapping("/contactForm/{id}")
    public String handleContactSubmission(@PathVariable("id") int id, @RequestParam("message") String message, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            Optional<Contact> contactOpt = contactService.getContactById(id);
            if (contactOpt.isPresent()) {
                Contact contact = contactOpt.get();
                contact.setMessage(message);
                contact.setStatus("processed");
                contact.setContactDate(LocalDateTime.now());

                // Cập nhật trạng thái trong cơ sở dữ liệu
                contactService.saveContactAdmin(contact);

                // Gửi email
                sendContactEmail(contact, message); // Truyền cả `contact` và `message`
                session.setAttribute("successMessage", "Your message has been sent successfully!");
            } else {
                session.setAttribute("errorMessage", "Contact not found.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Failed to send your message.");
            e.printStackTrace();
        }
        return "redirect:/admin/contact";
    }
    private void sendContactEmail(Contact contact, String messageContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(""); // mail nguời gửi
        helper.setTo(""); // mail nguời nhận
        helper.setSubject("Contact Response");

        String emailContent = "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Response to Your Inquiry</title>\n" +
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
                "            text-align: left;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .navbar-brand {\n" +
                "            display: inline-block;\n" +
                "            font-size: 20px;\n" +
                "            font-weight: 800;\n" +
                "            text-transform: uppercase;\n" +
                "            letter-spacing: 2px;\n" +
                "            color: #000000;\n" +
                "            margin-top: 22px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            margin-bottom: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            margin-bottom: 20px;\n" +
                "            line-height: 1.6;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <a class=\"navbar-brand\">Furni</a>\n" +
                "        </div>\n" +
                "        <h1>Response to Your Inquiry</h1>\n" +
                "        <p>Dear Customer,</p>\n" +  // Lời chào với "Dear Customer"
                "        <p>Thank you for reaching out to us. We appreciate your inquiry and would like to provide you with the following information:</p>\n" +
                "        <p><strong>" + messageContent + "</strong></p>\n" + // Nội dung phản hồi từ admin
                "        <p>If you have any further questions or need assistance, please do not hesitate to contact us.</p>\n" +
                "        <p>Thank you for choosing us!</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        helper.setText(emailContent, true);
        mailSender.send(message);
    }


}
