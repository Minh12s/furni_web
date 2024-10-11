//package com.example.furni.controllers.User;
//
//import com.example.furni.entity.Favorite;
//import com.example.furni.entity.Product;
//import com.example.furni.service.FavoriteService;
//import com.example.furni.service.ProductService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/")
//public class FavoriteController extends BaseController{
//    @Autowired
//    private ProductService productService;
//    @Autowired
//    private FavoriteService favoriteService;
//
//    @PostMapping("/add-to-favorite")
//    public String addToFavorite(HttpServletRequest request,
//                                @RequestParam int productId,
//                                @RequestParam(required = false) String slug,
//                                Model model) {
//
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
//
//        if (userId == null) {
//            model.addAttribute("error", "You need to login to manage your favorite list.");
//            return "User/login";
//        }
//
//        Product product = productService.findById(productId);
//        if (product == null) {
//            model.addAttribute("error", "Product does not exist.");
//            return "User/details";
//        }
//
//        // Kiểm tra xem sản phẩm đã có trong danh sách yêu thích chưa
//        boolean isFavorite = favoriteService.isProductInFavorites(userId, productId);
//
//        if (isFavorite) {
//            favoriteService.removeFavoriteByProductId(productId, userId);
//            request.getSession().setAttribute("successMessage", "Product removed from favorites.");
//        } else {
//            favoriteService.addToFavorite(userId, product);
//            request.getSession().setAttribute("successMessage", "Product added to favorites.");
//        }
//
//        // Chuyển hướng trở lại trang chi tiết sản phẩm
//        return "redirect:/product/details/" + (slug != null ? slug : product.getSlug());
//    }
//    @GetMapping("/favorite")
//    public String showFavoriteList(HttpServletRequest request, Model model, HttpSession session) {
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
//        List<Favorite> favoriteList = favoriteService.findFavoritesByUserId(userId);
//        model.addAttribute("favoriteList", favoriteList);
//        // Get the success message from session and remove it after retrieval
//        String successMessage = (String) session.getAttribute("favoriteMessage");
//        if (successMessage != null) {
//            model.addAttribute("favoriteMessage", successMessage);
//            session.removeAttribute("favoriteMessage");
//        }
//
//        return "User/favorite";  // Trả về trang favorite
//    }
//    @PostMapping("/favorite/remove")
//    public String removeFromFavorites(@RequestParam int favoriteId, HttpServletRequest request) {
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
//        favoriteService.removeFavoriteById(favoriteId, userId);
//        request.getSession().setAttribute("favoriteMessage", "The product has been removed from the favorite.");
//
//        return "redirect:/favorite";  // Sau khi xóa quay lại trang yêu thích
//    }
//
//    @PostMapping("/favorite/clear")
//    public String clearFavorites(HttpServletRequest request) {
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
//        favoriteService.clearFavoritesByUserId(userId);
//        request.getSession().setAttribute("favoriteMessage", "Favorite has been cleared.");
//
//        return "redirect:/favorite";
//    }
//
//
//}
