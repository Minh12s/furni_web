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
                                 @RequestParam(defaultValue = "5") int size, HttpSession session) {
        Page<Category> categoriesPage = categoryService.getCategoriesPaginated(page, size);
        model.addAttribute("categoriesPage", categoriesPage);
        // Get the success message from session and remove it after retrieval
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
    public String addCategory(@ModelAttribute("category") Category category, HttpSession session) {

        String slug = category.getCategoryName()
                .toLowerCase()             // Chuyển toàn bộ thành chữ thường
                .replaceAll("\\s+", "-");   // Thay thế khoảng trắng bằng dấu gạch nối
        category.setSlug(slug);
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
                               @ModelAttribute("category") Category categoryDetails, HttpSession session) {
        Category existingCategory = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingCategory.setCategoryName(categoryDetails.getCategoryName());

        // Update the slug based on the new category name
        String newSlug = categoryDetails.getCategoryName().toLowerCase().replaceAll("\\s+", "-");
        existingCategory.setSlug(newSlug);

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
