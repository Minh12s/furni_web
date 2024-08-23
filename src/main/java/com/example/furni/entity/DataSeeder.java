package com.example.furni.entity;

import com.example.furni.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder {

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final String[] PRODUCT_NAMES = {
            "Corner Sofa", "Single Sofa", "Sleeper Sofa", "Bench Sofa", "Sofa Bed",
            "Recliner Sofa", "Classic Sofa", "Modern Sofa", "Retro Sofa", "Contemporary Sofa",
            "Leather Sofa", "Fabric Sofa", "Wooden Sofa", "Velvet Sofa", "Metal Sofa",
            "Smart Sofa", "Multi-functional Sofa", "Outdoor Sofa", "Indoor Sofa", "Office Sofa",
            "Double Sofa", "Three-seater Sofa", "Four-seater Sofa", "Five-seater Sofa", "Simple Sofa",
            "Leather Upholstered Sofa", "Fabric Upholstered Sofa", "Wooden Frame Sofa", "Metal Frame Sofa", "Classic Sofa",
            "Modern Sofa", "Japanese Style Sofa", "Korean Style Sofa", "European Style Sofa",
            "Armless Sofa", "Armrest Sofa", "High-legged Sofa", "Low-legged Sofa", "Extended Sofa",
            "Multi-functional Double Sofa", "Comfortable Four-seater Sofa", "Stylish Double Sofa", "Practical Single Sofa",
            "Compact Sofa", "Elegant Sofa", "Adjustable Sofa", "Convertible Sofa", "Tufted Sofa",
            "Luxury Sofa", "Eco-friendly Sofa"
    };
    private static final String[] COLORS = {
            "Red", "Blue", "Green", "Gray", "Black", "White", "Yellow", "Orange", "Purple", "Brown"
    };

    private static final String IMAGE_PATH = "resources/static/images/product-";
    private static final int TOTAL_IMAGES = 50;
    private static final Random RANDOM = new Random();

    @PostConstruct
    @Transactional
    public void seedData() {
        seedSizes();
        seedBrands();
        seedCategories();
        seedMaterials();
        seedProducts();
    }

    private void seedSizes() {
        if (sizeRepository.count() == 0) {
            List<Size> sizes = List.of(
                    new Size("Small"),
                    new Size("Medium"),
                    new Size("Large"),
                    new Size("1-seater"),
                    new Size("2-seater"),
                    new Size("3-seater"),
                    new Size("4-seater")
            );
            sizeRepository.saveAll(sizes);
        }
    }

    private void seedBrands() {
        if (brandRepository.count() == 0) {
            List<Brand> brands = List.of(
                    new Brand("IKEA"),
                    new Brand("Ashley Furniture"),
                    new Brand("La-Z-Boy"),
                    new Brand("Natuzzi"),
                    new Brand("VinaSofa"),
                    new Brand("BAYA"),
                    new Brand("Vinaco"),
                    new Brand("Poliform")
            );
            brandRepository.saveAll(brands);
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("Corner Sofa", "corner-sofa"),
                    new Category("Sofa Bed", "sofa-bed"),
                    new Category("Single Sofa", "single-sofa"),
                    new Category("Sleeper Sofa", "sleeper-sofa"),
                    new Category("L-Shaped Sofa", "l-shaped-sofa"),
                    new Category("Bench Sofa", "bench-sofa"),
                    new Category("Recliner Sofa", "recliner-sofa"),
                    new Category("Classic Sofa", "classic-sofa")
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void seedMaterials() {
        if (materialRepository.count() == 0) {
            List<Material> materials = List.of(
                    new Material("Fabric"),
                    new Material("Genuine Leather"),
                    new Material("Artificial Leather"),
                    new Material("Wood"),
                    new Material("Canvas"),
                    new Material("Velvet"),
                    new Material("Metal"),
                    new Material("Foam")
            );
            materialRepository.saveAll(materials);
        }
    }

    private void seedProducts() {
        if (productRepository.count() == 0) {
            for (String productName : PRODUCT_NAMES) {
                String slug = productName.toLowerCase().replaceAll("[^a-z0-9]+", "-");

                // In ra slug để kiểm tra
                System.out.println("Generated Slug: " + slug);

                double price = roundToNearestFive(100 + RANDOM.nextDouble() * 500);
                int qty = RANDOM.nextInt(100) + 1;
                String color = COLORS[RANDOM.nextInt(COLORS.length)];
                double weight = roundToNearestHalf(RANDOM.nextDouble() * 100);
                double height = roundToNearestHalf(50 + RANDOM.nextDouble() * 150);
                double length = roundToNearestHalf(100 + RANDOM.nextDouble() * 300);
                String thumbnail = encodeImageToBase64(RANDOM.nextInt(TOTAL_IMAGES) + 1);

                Category category = categoryRepository.findById(RANDOM.nextInt((int) categoryRepository.count()) + 1).orElse(null);
                Brand brand = brandRepository.findById(RANDOM.nextInt((int) brandRepository.count()) + 1).orElse(null);
                Material material = materialRepository.findById(RANDOM.nextInt((int) materialRepository.count()) + 1).orElse(null);
                Size size = sizeRepository.findById(RANDOM.nextInt((int) sizeRepository.count()) + 1).orElse(null);

                Product product = new Product();
                product.setProductName(productName);
                product.setSlug(slug);
                product.setPrice(price);
                product.setQty(qty);
                product.setColor(color);
                product.setWeight(weight);
                product.setHeight(height);
                product.setLength(length);
                product.setThumbnail(thumbnail);
                product.setCategory(category);
                product.setBrand(brand);
                product.setMaterial(material);
                product.setSize(size);

                productRepository.save(product);
            }
        }
    }

    private double roundToNearestFive(double value) {
        return Math.round(value / 5.0) * 5.0;
    }

    private double roundToNearestHalf(double value) {
        return Math.round(value * 2) / 2.0;
    }

    private String encodeImageToBase64(int imageNumber) {
        String imagePath = "static/images/product-" + imageNumber + ".jpg";
        try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
            if (imageStream == null) {
                throw new IOException("Image not found: " + imagePath);
            }
            byte[] imageBytes = imageStream.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
