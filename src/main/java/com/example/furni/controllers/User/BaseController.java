package com.example.furni.controllers.User;
import com.example.furni.service.CartService;
import com.example.furni.service.NotificationService;
import com.example.furni.service.FavoriteService;
import com.example.furni.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public abstract class BaseController {

    @Autowired
    private CartService cartService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private NotificationService notificationService;

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
    @ModelAttribute
    public void addNotifications(HttpServletRequest request, Model model) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId != null) {
            // Lấy danh sách thông báo và số lượng thông báo
            List<Map<String, String>> notifications = notificationService.getNotificationsByUserId(userId);
            long notificationCount = notificationService.getNotificationCountByUserId(userId);

            model.addAttribute("notifications", notifications);
            model.addAttribute("notificationCount", notificationCount); // Thêm số lượng thông báo
        } else {
            model.addAttribute("notifications", new ArrayList<>());
            model.addAttribute("notificationCount", 0); // Không có thông báo nếu chưa đăng nhập
        }
    }
}
