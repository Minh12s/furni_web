package com.example.furni.controllers.admin;

import com.example.furni.entity.Blog;
import com.example.furni.entity.Brand;
import com.example.furni.entity.Product;
import com.example.furni.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public String getAllProducts(Model model, @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "9") int size) {
        Page<Product> productPage = productService.getProductsPaginated(page, size);
        model.addAttribute("products", productPage);
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
                             @RequestParam("thumbnailFile") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                product.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Gán giá trị ngày giờ hiện tại nếu blogDate là null

        productService.save(product);
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
                                @RequestParam("thumbnailFile") MultipartFile file) {
        // Tìm sản phẩm hiện tại trong cơ sở dữ liệu
        Product existingProduct = productService.findById(id);

        if (existingProduct != null) {
            // Cập nhật các thuộc tính của sản phẩm từ form
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

            // Nếu có file hình ảnh mới, cập nhật thumbnail
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(bytes);
                    existingProduct.setThumbnail(base64Image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Lưu sản phẩm cập nhật
            productService.save(existingProduct);
        }

        return "redirect:/admin/products";
    }


    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
