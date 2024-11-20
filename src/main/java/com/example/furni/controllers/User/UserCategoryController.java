package com.example.furni.controllers.User;

import com.example.furni.entity.*;
import com.example.furni.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/")
public class UserCategoryController extends BaseController{

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/category")
    public String category(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "9") int size,
                           @RequestParam(required = false) String slug,
                           @RequestParam(required = false) Double minPrice,
                           @RequestParam(required = false) Double maxPrice,
                           @RequestParam(required = false) String sizeFilter,
                           @RequestParam(required = false) String color,
                           @RequestParam(required = false) String material,
                           @RequestParam(required = false) Integer brandId) {

        // Lấy tất cả các danh mục
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        // Lọc sản phẩm theo tiêu chí
        Page<Product> productPage;

        if (slug != null && !slug.isEmpty()) {
            productPage = productService.filterProductsByCategoryWithCriteria(slug, minPrice, maxPrice, sizeFilter, color, material, brandId, page, size);
        } else {
            productPage = productService.filterProductsWithMultipleCriteria( minPrice, maxPrice, sizeFilter, color, material, brandId, page, size);
        }

        // Lấy danh sách sản phẩm sau khi lọc
        List<Product> filteredProducts = productPage.getContent();

        // Lấy tất cả các màu sắc từ toàn bộ sản phẩm, không phụ thuộc vào kết quả lọc
        Set<String> uniqueColors = productService.getAllAvailableColors(); // Hàm này trả về danh sách màu sắc không bị lặp
        // Tính rating cho mỗi sản phẩm
        Map<Integer, Double> productRatings = new HashMap<>();
        for (Product product : filteredProducts) {
            double averageRating = reviewService.calculateAverageRating(product.getId());
            productRatings.put(product.getId(), averageRating);
        }

        // Thêm các thuộc tính vào model
        model.addAttribute("productRatings", productRatings);

        model.addAttribute("products", filteredProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedCategory", slug);
        model.addAttribute("uniqueColors", uniqueColors);  // Truyền danh sách tất cả các màu
        model.addAttribute("selectedColor", color);  // Màu đã chọn

        // Lấy các danh sách cho các trường lọc
        List<Size> sizes = sizeService.getAllSize();
        List<Material> materials = materialService.getAllMaterials();
        List<Brand> brands = brandService.getAllBrands();

        model.addAttribute("sizes", sizes);
        model.addAttribute("materials", materials);
        model.addAttribute("brands", brands);
        model.addAttribute("hasProducts", !filteredProducts.isEmpty());


        return "User/category";
    }



    @GetMapping("product/details/{slug}")
    public String getProductDetails(@PathVariable String slug, Model model, HttpServletRequest request) {
        Product product = productService.findBySlug(slug);
        if (product != null) {
            model.addAttribute("product", product);

            // Lấy 4 sản phẩm cùng danh mục nhưng không bao gồm sản phẩm hiện tại
            Page<Product> relatedProducts = productService.getRelatedProducts(product.getCategory().getId(), product.getId(), 4);
            // Tính rating cho từng sản phẩm liên quan
            Map<Integer, Double> relatedProductRatings = new HashMap<>();
            for (Product relatedProduct : relatedProducts) {
                double averageRating = reviewService.calculateAverageRating(relatedProduct.getId());
                relatedProductRatings.put(relatedProduct.getId(), averageRating);
            }
            model.addAttribute("relatedProducts", relatedProducts.getContent());
            model.addAttribute("relatedProductRatings", relatedProductRatings);


            // Lấy thông báo thành công từ session và xóa sau khi lấy
            String successMessage = (String) request.getSession().getAttribute("successMessage");
            if (successMessage != null) {
                model.addAttribute("successMessage", successMessage);
                request.getSession().removeAttribute("successMessage");
            }

            // Lấy thông báo lỗi từ session và xóa sau khi lấy
            String errorMessage = (String) request.getSession().getAttribute("errorDetailMessage");
            if (errorMessage != null) {
                model.addAttribute("errorDetailMessage", errorMessage);
                request.getSession().removeAttribute("errorDetailMessage");
            }

            // Tính toán các thông tin đánh giá
            double averageRating = reviewService.calculateAverageRating(product.getId());
            int reviewCount = reviewService.countApprovedReviews(product.getId());
            int totalSales = productService.getTotalSales(product.getId());
            List<Review> approvedComments = reviewService.getApprovedCommentsByProductId(product.getId());
            // Thêm các thông tin vào model
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("reviewCount", reviewCount);
            model.addAttribute("totalSales", totalSales);
            model.addAttribute("approvedComments", approvedComments);
            return "User/details"; // Trả về view hiển thị chi tiết sản phẩm
        } else {
            return "redirect:/error"; // Chuyển hướng đến trang lỗi nếu không tìm thấy sản phẩm
        }
    }


}
