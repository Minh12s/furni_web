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

    @GetMapping("/sizes")
    public String showSizes(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            HttpSession session) {
        Page<Size> sizesPage = sizeService.getSizesPaginated(page, size);
        model.addAttribute("sizesPage", sizesPage);
        model.addAttribute("size", size);

        // Lấy thông báo thành công từ session và xóa sau khi lấy
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
    public String addSize(@ModelAttribute Size size, HttpSession session, Model model) {
        // Kiểm tra tính hợp lệ
        if (size.getSizeName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Size name cannot be empty.");
            return "admin/Size/addSize";
        }

        if (sizeService.isSizeNameExists(size.getSizeName())) {
            model.addAttribute("errorMessage", "Size name already exists.");
            return "admin/Size/addSize";
        }

        sizeService.save(size);
        session.setAttribute("successMessage", "Size added successfully!");
        return "redirect:/admin/sizes";
    }

    @GetMapping("/editSize/{id}")
    public String showEditSizeForm(@PathVariable int id, Model model) {
        Optional<Size> size = sizeService.findById(id);
        if (size.isPresent()) {
            model.addAttribute("size", size.get());
            return "admin/Size/editSize";
        } else {
            return "redirect:/admin/sizes";
        }
    }

    @PostMapping("/editSize")
    public String editSize(@ModelAttribute Size size, HttpSession session, Model model) {
        // Kiểm tra tính hợp lệ
        if (size.getSizeName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Size name cannot be empty.");
            return "admin/Size/editSize";
        }

        if (sizeService.isSizeNameExists(size.getSizeName(), size.getId())) {
            model.addAttribute("errorMessage", "Size name already exists.");
            return "admin/Size/editSize";
        }

        sizeService.save(size);
        // Thêm thông báo thành công vào session
        session.setAttribute("successMessage", "Size updated successfully!");
        return "redirect:/admin/sizes";
    }

    @PostMapping("/deleteSize/{id}")
    public String deleteSize(@PathVariable int id,HttpSession session) {
        sizeService.deleteById(id);
        session.setAttribute("successMessage", "Size deleted successfully!");
        return "redirect:/admin/sizes";
    }
}
