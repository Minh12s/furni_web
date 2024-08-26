package com.example.furni.controllers.User;

import com.example.furni.entity.Category;
import com.example.furni.entity.Product;
import com.example.furni.service.ProductService;
import com.example.furni.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserCategoryController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public String Category(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "9") int size,
                           @RequestParam(required = false) String slug) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Page<Product> productPage;
        if (slug != null) {
            productPage = productService.getProductsByCategoryPaginated(slug, page, size);
        } else {
            productPage = productService.getProductsPaginated(page, size);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedCategory", slug);

        return "User/category";
    }
}
