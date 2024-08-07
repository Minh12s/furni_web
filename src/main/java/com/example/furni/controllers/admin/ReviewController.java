package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ReviewController {
    @GetMapping("/reviews")
    public String Review(){
        return "admin/Review/review";
    }
    @GetMapping("/listreview")
    public String ListReview(){
        return "admin/Review/ListReview";
    }
    @GetMapping("/detailreview")
    public String DetailReview(){
        return "admin/Review/DetailReview";
    }
}
