package com.example.furni.controllers.admin;

import com.example.furni.entity.Size;
import com.example.furni.service.SizeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping("/size")
    public String sizeList(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session ) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Size> sizePage = sizeService.findAll(pageable);
        model.addAttribute("sizes", sizePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sizePage.getTotalPages());
        // Get the success message from session and remove it after retrieval
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }
        return "admin/Size/size";
    }

    @GetMapping("/addSize")
    public String showAddSizeForm(Model model) {
        model.addAttribute("size", new Size());
        return "admin/Size/addSize";
    }

    @PostMapping("/addSize")
    public String addSize(@ModelAttribute Size size, HttpSession session) {
        sizeService.save(size);
        session.setAttribute("successMessage", "Size added successfully!");
        return "redirect:/admin/size";
    }

    @GetMapping("/editSize/{id}")
    public String showEditSizeForm(@PathVariable int id, Model model) {
        Optional<Size> size = sizeService.findById(id);
        if (size.isPresent()) {
            model.addAttribute("size", size.get());
            return "admin/Size/editSize";
        } else {
            return "redirect:/admin/size";
        }
    }

    @PostMapping("/editSize")
    public String editSize(@ModelAttribute Size size) {
        sizeService.save(size);
        return "redirect:/admin/size";
    }

    @PostMapping("/deleteSize/{id}")
    public String deleteSize(@PathVariable int id) {
        sizeService.deleteById(id);
        return "redirect:/admin/size";
    }
}
