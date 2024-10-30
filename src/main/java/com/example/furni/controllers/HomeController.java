package com.example.furni.controllers;

import com.example.furni.controllers.User.BaseController;
import com.example.furni.service.ReviewService;
import org.springframework.ui.Model;
import com.example.furni.entity.Product;
import com.example.furni.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {
    @Autowired
    private HomeService homeService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String Home(){
        return "User/index";
    }
    @GetMapping("/details")
    public String Detail(){
        return "User/details";
    }
    @GetMapping("/cart")
    public String Cart(){
        return "User/cart";
    }
    @GetMapping("/about")
    public String About(){
        return "User/about";
    }
    @GetMapping("/checkout")
    public String Checkout(){
        return "User/checkout";
    }
    @GetMapping("/thankyou")
    public String Thankyou(){
        return "User/thankyou";
    }


    @GetMapping("/search")
    public String search(@RequestParam("searchString") String searchString, Model model) {
        // Tìm các sản phẩm theo từ khóa
        List<Product> products = homeService.searchProducts(searchString);
        model.addAttribute("products", products);

        // Tính rating cho từng sản phẩm
        Map<Integer, Double> productRatings = new HashMap<>();
        for (Product product : products) {
            double averageRating = reviewService.calculateAverageRating(product.getId());
            productRatings.put(product.getId(), averageRating);
        }
        model.addAttribute("productRatings", productRatings);

        return "User/search";
    }

}