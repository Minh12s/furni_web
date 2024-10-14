package com.example.furni.controllers.User;

import com.example.furni.entity.Favorite;
import com.example.furni.entity.Product;
import com.example.furni.entity.User;
import com.example.furni.service.FavoriteService;
import com.example.furni.service.ProductService;
import com.example.furni.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class FavoriteController extends BaseController{
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private FavoriteService favoriteService;
    @PostMapping("/add-to-favorite")
    public String addToFavorite(HttpServletRequest request,
                                @RequestParam int productId,
                                @RequestParam(required = false) String slug,
                                Model model) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            model.addAttribute("error", "You need to login to add products to favorites.");
            return "User/login";
        }

        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute("error", "Product does not exist.");
            return "User/details";
        }

        User user = userService.findById(userId);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "User/details";
        }

        boolean isFavorite = favoriteService.isFavorite(user, product);  // Kiểm tra sản phẩm đã có trong danh sách yêu thích chưa

        if (isFavorite) {
            // Nếu đã tồn tại, xóa khỏi danh sách yêu thích
            favoriteService.removeFromFavorite(user, product);
            request.getSession().setAttribute("successMessage", "Product removed from favorites.");
        } else {
            // Nếu chưa tồn tại, thêm vào danh sách yêu thích
            favoriteService.addToFavorite(user, product);
            request.getSession().setAttribute("successMessage", "Product added to favorites.");
        }
        return "redirect:/product/details/" + (slug != null ? slug : product.getSlug());
    }
    @GetMapping("/favorite")
    public String showFavorites(Model model, HttpServletRequest request, HttpSession session) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        // Get favorite products by user ID
        List<Favorite> favoriteProducts = favoriteService.getFavoritesByUserId(userId);
        model.addAttribute("favorites", favoriteProducts);
        String successMessage = (String) session.getAttribute("favoriteMessage");
        if (successMessage != null) {
            model.addAttribute("favoriteMessage", successMessage);
            session.removeAttribute("favoriteMessage");
        }

        return "User/favorite"; // Return the view name
    }
    @PostMapping("/favorite/remove")
    public String removeFavorite(HttpServletRequest request,
                                 @RequestParam int productId) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        Product product = productService.findById(productId);
        if (product == null) {
            request.getSession().setAttribute("error", "Product does not exist.");
            return "redirect:/favorite";
        }

        User user = userService.findById(userId);
        if (user == null) {
            request.getSession().setAttribute("error", "User not found.");
            return "redirect:/favorite";
        }

        favoriteService.removeFromFavorite(user, product);
        request.getSession().setAttribute("favoriteMessage", "The product has been removed from the favorite.");

        return "redirect:/favorite"; // Redirect back to favorites page
    }
    @PostMapping("/favorite/clear")
    public String clearFavorites(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        favoriteService.clearFavoritesByUserId(userId);
        request.getSession().setAttribute("favoriteMessage", "All favorites have been cleared.");

        return "redirect:/favorite"; // Redirect back to favorites page
    }



}
