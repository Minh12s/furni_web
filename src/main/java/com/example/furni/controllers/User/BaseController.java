package com.example.furni.controllers.User;
import com.example.furni.service.CartService;
import com.example.furni.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public abstract class BaseController {

    @Autowired
    private CartService cartService;
    @Autowired
    private FavoriteService favoriteService;

    @ModelAttribute
    public void addCartItemCount(HttpServletRequest request, Model model) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId != null) {
            int cartItemCount = cartService.getCartItemCount(userId);
            model.addAttribute("cartItemCount", cartItemCount);
        } else {
            model.addAttribute("cartItemCount", 0);
        }
    }
    @ModelAttribute
    public void addFavoriteItemCount(HttpServletRequest request, Model model) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId != null) {
            int favoriteItemCount = favoriteService.getFavoriteItemCount(userId);
            model.addAttribute("favoriteItemCount", favoriteItemCount);
        } else {
            model.addAttribute("favoriteItemCount", 0);
        }
    }
}
