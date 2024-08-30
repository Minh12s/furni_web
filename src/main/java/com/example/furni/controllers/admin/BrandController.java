package com.example.furni.controllers.admin;

import com.example.furni.entity.Brand;
import com.example.furni.entity.Material;
import com.example.furni.service.BrandService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class BrandController {
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brand")
    public String getAllBrands(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               HttpSession session) {
        Page<Brand> brandsPage = brandService.getBrandsPaginated(page, size);
        model.addAttribute("brandsPage", brandsPage);

        // Lấy thông báo thành công từ session và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }
        return "admin/brand/brand";
    }

    @GetMapping("/editBrand/{id}")
    public String getBrandById(@PathVariable int id, Model model) {
        Optional<Brand> brand = brandService.getBrandById(id);
        if (brand.isPresent()) {
            model.addAttribute("brand", brand.get());
            return "/admin/brand/editBrand";
        } else {
            return "redirect:/admin/brand";
        }
    }

    @GetMapping("/addBrand")
    public String AddBrand(Model model) {
        model.addAttribute("brand", new Brand());
        return "/admin/brand/addBrand";
    }

    @PostMapping("/addBrand")
    public String AddBrand(@ModelAttribute Brand brand, HttpSession session, Model model) {
        // Kiểm tra tính hợp lệ
        if (brand.getBrandName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Brand name cannot be empty.");
            return "/admin/brand/addBrand";
        }

        if (brandService.isBrandNameExists(brand.getBrandName())) {
            model.addAttribute("errorMessage", "Brand name already exists.");
            return "/admin/brand/addBrand";
        }

        brandService.saveBrand(brand);
        session.setAttribute("successMessage", "Brand added successfully!");
        return "redirect:/admin/brand";
    }

    @PostMapping("/editBrand/{id}")
    public String updateBrand(@PathVariable Integer id, @ModelAttribute Brand brand, HttpSession session, Model model) {
        // Kiểm tra tính hợp lệ
        if (brand.getBrandName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Brand name cannot be empty.");
            return "/admin/brand/editBrand";
        }

        if (brandService.isBrandNameExists(brand.getBrandName(), id)) {
            model.addAttribute("errorMessage", "Brand name already exists.");
            return "/admin/brand/editBrand";
        }

        brand.setId(id);
        brandService.saveBrand(brand);
        session.setAttribute("successMessage", "Brand updated successfully!");
        return "redirect:/admin/brand";
    }

    @PostMapping("/deleteBrand/{id}")
    public String deleteClassRoom(@PathVariable Integer id, HttpSession session) {
        brandService.deleteBrand(id);
        session.setAttribute("successMessage", "Brand deleted successfully!");
        return "redirect:/admin/brand";
    }
}
