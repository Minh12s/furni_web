package com.example.furni.controllers.admin;

import com.example.furni.entity.Blog;
import com.example.furni.entity.Brand;
import com.example.furni.entity.Product;
import com.example.furni.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private SizeService sizeService;

    @GetMapping("/products")
    public String getAllProducts(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "9") int size,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) Integer categoryId,
                                 @RequestParam(required = false) Double minPrice,
                                 @RequestParam(required = false) Double maxPrice,
                                 HttpSession session) {
        Page<Product> productPage = productService.filterProducts(name, categoryId, minPrice, maxPrice, page, size);
        model.addAttribute("products", productPage);
        model.addAttribute("categories", categoryService.getAllCategories());
        // Lấy thông báo thành công từ session và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }
        return "admin/Product/product";
    }
    @GetMapping("/products/productDetail/{id}")
    public String getProductDetail(@PathVariable("id") int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "admin/Product/productDetail";
    }


    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("materials", materialService.getAllMaterials());
        model.addAttribute("sizes", sizeService.getAllSize());
        return "/admin/Product/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("thumbnailFile") MultipartFile file,
                             HttpSession session, Model model) {
        List<String> errorMessages = new ArrayList<>();

        if (product.getProductName().trim().isEmpty()) {
            errorMessages.add("Product name cannot be empty.");
        }
        if (productService.isProductNameExists(product.getProductName())) {
            errorMessages.add("Product Name already exists.");
        }
        if (product.getPrice() <= 0) {
            errorMessages.add("Price must be greater than zero.");
        }

        if (product.getQty() < 0) {
            errorMessages.add("Quantity cannot be negative.");
        }

        if (product.getColor().trim().isEmpty()) {
            errorMessages.add("Color cannot be empty.");
        }

        if (product.getWeight() <= 0) {
            errorMessages.add("Weight must be greater than zero.");
        }

        if (product.getHeight() <= 0) {
            errorMessages.add("Height must be greater than zero.");
        }

        if (product.getLength() <= 0) {
            errorMessages.add("Length must be greater than zero.");
        }

        if (file.isEmpty()) {
            errorMessages.add("Thumbnail cannot be empty.");
        } else {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                product.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                errorMessages.add("Error processing the thumbnail.");
            }
        }

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("materials", materialService.getAllMaterials());
            model.addAttribute("sizes", sizeService.getAllSize());
            return "admin/Product/addProduct";
        }

        productService.save(product);
        session.setAttribute("successMessage", "Product added successfully!");
        return "redirect:/admin/products";
    }


    @GetMapping("/editProduct/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("materials", materialService.getAllMaterials());
        model.addAttribute("sizes", sizeService.getAllSize());
        return "admin/Product/editProduct";
    }

    @PostMapping("/editProduct/{id}")
    public String updateProduct(@PathVariable("id") int id,
                                @ModelAttribute("product") Product product,
                                @RequestParam("thumbnailFile") MultipartFile file,
                                HttpSession session, Model model) {
        List<String> errorMessages = new ArrayList<>();

        // Kiểm tra các lỗi
        if (product.getProductName().trim().isEmpty()) {
            errorMessages.add("Product name cannot be empty.");
        }
        if (productService.isProductNameExists(product.getProductName(), id)) {
            errorMessages.add("Product name already exists.");
        }
        if (product.getPrice() <= 0) {
            errorMessages.add("Price must be greater than zero.");
        }
        if (product.getQty() < 0) {
            errorMessages.add("Quantity cannot be negative.");
        }
        if (product.getColor().trim().isEmpty()) {
            errorMessages.add("Color cannot be empty.");
        }
        if (product.getWeight() <= 0) {
            errorMessages.add("Weight must be greater than zero.");
        }
        if (product.getHeight() <= 0) {
            errorMessages.add("Height must be greater than zero.");
        }
        if (product.getLength() <= 0) {
            errorMessages.add("Length must be greater than zero.");
        }

        // Xử lý thumbnail
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                product.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                errorMessages.add("Error processing the thumbnail.");
            }
        }

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("materials", materialService.getAllMaterials());
            model.addAttribute("sizes", sizeService.getAllSize());
            return "admin/Product/editProduct";
        }

        // Cập nhật sản phẩm
        Product existingProduct = productService.findById(id);
        if (existingProduct != null) {
            existingProduct.setProductName(product.getProductName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQty(product.getQty());
            existingProduct.setColor(product.getColor());
            existingProduct.setWeight(product.getWeight());
            existingProduct.setHeight(product.getHeight());
            existingProduct.setLength(product.getLength());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setMaterial(product.getMaterial());
            existingProduct.setSize(product.getSize());

            if (!file.isEmpty()) {
                existingProduct.setThumbnail(product.getThumbnail());
            }

            productService.save(existingProduct);
            session.setAttribute("successMessage", "Product updated successfully!");
        } else {
            errorMessages.add("Product not found.");
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("materials", materialService.getAllMaterials());
            model.addAttribute("sizes", sizeService.getAllSize());
            return "admin/Product/editProduct";
        }

        return "redirect:/admin/products";
    }



    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") int id,HttpSession session) {
        productService.delete(id);
        session.setAttribute("successMessage", "Product deleted successfully!");
        return "redirect:/admin/products";
    }
}
