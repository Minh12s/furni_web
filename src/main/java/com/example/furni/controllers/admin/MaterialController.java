package com.example.furni.controllers.admin;
import org.springframework.data.domain.Page;
import com.example.furni.entity.Material;
import com.example.furni.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/materials")
    public String showMaterials(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Page<Material> blogsPage = materialService.getMaterialsPaginated(page, size);
        model.addAttribute("materialsPage", blogsPage);
        return "admin/Material/material";
    }

    @GetMapping("/addMaterial")
    public String showAddMaterialForm(Model model) {
        model.addAttribute("material", new Material());
        return "admin/Material/addMaterial";
    }

    @PostMapping("/addMaterial")
    public String addBlog(@ModelAttribute("material") Material material) {
        materialService.saveMaterial(material);
        return "redirect:/admin/materials";
    }


    @GetMapping("/editMaterial/{id}")
    public String showEditMaterialForm(@PathVariable int id, Model model) {
        Material material = materialService.getMaterialById(id).orElseThrow(() -> new RuntimeException("Material not found"));
        model.addAttribute("material", material);
        return "admin/Material/editMaterial";
    }

    @PostMapping("/editMaterial/{id}")
    public String editMaterial(@PathVariable int id,
                           @ModelAttribute("material") Material materialDetails) {
        Material existingMaterial = materialService.getMaterialById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        existingMaterial.setMaterialName(materialDetails.getMaterialName());
        materialService.updateMaterial(id, existingMaterial);
        return "redirect:/admin/materials";
    }



    @PostMapping("/deleteMaterial/{id}")
    public String deleteMaterial(@PathVariable int id) {
        materialService.deleteMaterial(id);
        return "redirect:/admin/materials";
    }
}
