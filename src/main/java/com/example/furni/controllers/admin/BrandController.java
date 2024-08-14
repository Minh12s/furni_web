package com.example.furni.controllers.admin;

import com.example.furni.entity.Brand;
import com.example.furni.entity.Material;
import com.example.furni.service.BrandService;
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
    public String getAllBrands(Model model, @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        Page<Brand> brandsPage = brandService.getBrandsPaginated(page, size);
        model.addAttribute("brandsPage", brandsPage);
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
    public String AddBrand(@ModelAttribute Brand brand) {
        brandService.saveBrand(brand);
        return "redirect:/admin/brand";
    }
    @PostMapping("/editBrand/{id}")
    public String updateBrand(@PathVariable Integer id, @ModelAttribute Brand brand) {
        brand.setId(id);
        brandService.saveBrand(brand);
        return "redirect:/admin/brand";
    }
    @PostMapping ("/deleteBrand/{id}")
    public String deleteClassRoom(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return "redirect:/admin/brand";
    }
}
