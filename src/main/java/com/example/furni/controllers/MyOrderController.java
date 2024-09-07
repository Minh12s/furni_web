package com.example.furni.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/MyOrder")
public class MyOrderController {

    @GetMapping("/MyOrder")
    public String MyOrder(){
        return "MyOrder/MyOrder";
    }
//    @GetMapping("/ChangePassword")
//    public String ChangePassword(){
//        return "MyOrder/ChangePassword";
//    }
    @GetMapping("/Profile")
    public String Profile(){
        return "MyOrder/Profile";
    }
    @GetMapping("/EditProfile")
    public String EditProfile(){
        return "MyOrder/EditProfile";
    }
    @GetMapping("/OrderCancel")
    public String OrderCancel(){
        return "MyOrder/OrderCancel";
    }
    @GetMapping("/OrderPending")
    public String OrderPending(){
        return "MyOrder/OrderPending";
    }
    @GetMapping("/OrderConfirmed")
    public String OrderConfirmed(){
        return "MyOrder/OrderConfirmed";
    }
    @GetMapping("/OrderShipping")
    public String OrderShipping(){
        return "MyOrder/OrderShipping";
    }
    @GetMapping("/OrderShipped")
    public String OrderShipped(){
        return "MyOrder/OrderShipped";
    }
    @GetMapping("/OrderComplete")
    public String OrderComplete(){
        return "MyOrder/OrderComplete";
    }
    @GetMapping("/OrderReturn")
    public String OrderReturn(){
        return "MyOrder/OrderReturn";
    }
    @GetMapping("/OrderDetail")
    public String OrderDetail(){
        return "MyOrder/OrderDetail";
    }
    @GetMapping("/Review")
    public String Review(){
        return "MyOrder/Review";
    }
    @GetMapping("/RequestRefund")
    public String RequestRefund(){
        return "MyOrder/RequestRefund";
    }
    @GetMapping("/ReasonCancel")
    public String ReasonCancel(){
        return "MyOrder/ReasonCancel";
    }

}
