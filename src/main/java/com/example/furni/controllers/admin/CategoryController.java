package com.example.furni.controllers.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.furni.entity.Category;
import com.example.furni.service.CategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public String showCategories(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 HttpSession session) {
        Page<Category> categoriesPage = categoryService.getCategoriesPaginated(page, size);
        model.addAttribute("categoriesPage", categoriesPage);
        model.addAttribute("size", size);

        // Lấy thông báo thành công từ session và xóa sau khi lấy
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

        return "admin/Category/category";
    }

    @GetMapping("/addCategory")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/Category/addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("category") Category category,
                              @RequestParam("imageFile") MultipartFile file,
                              HttpSession session, Model model) {

        String slug = category.getCategoryName()
                .toLowerCase()
                .replaceAll("\\s+", "-");
        category.setSlug(slug);

        // Kiểm tra tính hợp lệ
        if (category.getCategoryName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Category name cannot be empty.");
            return "admin/Category/addCategory";
        }

        if (categoryService.isCategoryNameExists(category.getCategoryName())) {
            model.addAttribute("errorMessage", "Category name already exists.");
            return "admin/Category/addCategory";
        }

        // Xử lý ảnh
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                category.setImage(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error processing the image.");
                return "admin/Category/addCategory";
            }
        }

        categoryService.saveCategory(category);
        session.setAttribute("successMessage", "Category added successfully!");
        return "redirect:/admin/category";
    }

    @GetMapping("/editCategory/{id}")
    public String showEditCategoryForm(@PathVariable int id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        model.addAttribute("category", category);
        return "admin/Category/editCategory";
    }

    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable int id,
                               @ModelAttribute("category") Category category,
                               @RequestParam("imageFile") MultipartFile file,
                               HttpSession session, Model model) {
        Category existingCategory = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingCategory.setCategoryName(category.getCategoryName());

        // Update the slug based on the new category name
        String newSlug = category.getCategoryName().toLowerCase().replaceAll("\\s+", "-");
        existingCategory.setSlug(newSlug);

        // Xử lý ảnh
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                existingCategory.setImage(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error processing the image.");
                model.addAttribute("category", existingCategory);
                return "admin/Category/editCategory";
            }
        }

        // Kiểm tra tính hợp lệ
        if (category.getCategoryName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Category name cannot be empty.");
            return "admin/Category/editCategory";
        }

        if (categoryService.isCategoryNameExists(category.getCategoryName(), id)) {
            model.addAttribute("errorMessage", "Category name already exists.");
            return "admin/Category/editCategory";
        }

        categoryService.saveCategory(existingCategory);
        session.setAttribute("successMessage", "Category updated successfully!");
        return "redirect:/admin/category";
    }

    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        try {
            categoryService.deleteCategory(id);
            session.setAttribute("successMessage", "Category deleted successfully!");
        } catch (IllegalStateException e) {
            session.setAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            session.setAttribute("errorMessage", "Category not found.");
        }
        return "redirect:/admin/category";
    }

}
