package com.example.furni.service;

import com.example.furni.entity.Contact;
import com.example.furni.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public Page<Contact> getAllContacts(int page, int size) {
        Sort sortByStatusAndDate = Sort.by(Sort.Order.asc("status"), Sort.Order.desc("contactDate"));
        return contactRepository.findAll(PageRequest.of(page, size, sortByStatusAndDate));
    }
    public void saveContactAdmin(Contact contact) {
        contact.setContactDate(LocalDateTime.now());
        contactRepository.save(contact);
    }
    public void saveContact(Contact contact) {
        contact.setContactDate(LocalDateTime.now());
        contactRepository.save(contact);

        // Gửi email với thông tin từ form liên hệ
        String toEmail = ""; // Địa chỉ email mà bạn muốn nhận thông báo liên hệ
        String subject = "New Contact Form Submission";
        String body = "Name: " + contact.getName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Message: " + contact.getMessage() ;


        sendSimpleEmail(toEmail, subject, body);
    }

    private void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
    public Optional<Contact> getContactById(int id) {
        return contactRepository.findById(id);
    }

}
