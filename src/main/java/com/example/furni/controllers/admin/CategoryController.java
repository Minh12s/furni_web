package com.example.furni.controllers.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.furni.entity.Category;
import com.example.furni.service.CategoryService;

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

        return "admin/Category/category";
    }


    @GetMapping("/addCategory")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/Category/addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("category") Category category, HttpSession session,Model model) {

        String slug = category.getCategoryName()
                .toLowerCase()             // Chuyển toàn bộ thành chữ thường
                .replaceAll("\\s+", "-");   // Thay thế khoảng trắng bằng dấu gạch nối
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
                               @ModelAttribute("category") Category category, HttpSession session, Model model) {
        Category existingCategory = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingCategory.setCategoryName(category.getCategoryName());

        // Update the slug based on the new category name
        String newSlug = category.getCategoryName().toLowerCase().replaceAll("\\s+", "-");
        existingCategory.setSlug(newSlug);

        // Kiểm tra tính hợp lệ
        if (category.getCategoryName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Category name cannot be empty.");
            return "admin/Category/editCategory";
        }

        if (categoryService.isCategoryNameExists(category.getCategoryName(), id)) {
            model.addAttribute("errorMessage", "Category name already exists.");
            return "admin/Category/editCategory";
        }

        categoryService.updateCategory(id, existingCategory);
        // Add success message to session
        session.setAttribute("successMessage", "Category updated successfully!");
        return "redirect:/admin/category";
    }


    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        categoryService.deleteCategory(id);
        session.setAttribute("successMessage", "Category deleted successfully!");
        return "redirect:/admin/category";
    }
}
