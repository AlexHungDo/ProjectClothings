package com.example.demo.Controller;

import com.example.demo.Myconstant.MyConstant;
import com.example.demo.Reponsitory.BuyProductRepo;
import com.example.demo.Reponsitory.UserRepository;
import com.example.demo.Reponsitory.WhiteListRepo;
import com.example.demo.Reponsitory.WhiteListRepo2;
import com.example.demo.Service.UserBuyProductService;
import com.example.demo.Untity.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class BuyProductController {
    @Autowired
    BuyProductRepo buyProductRepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WhiteListRepo whiteListRepo;
    @Autowired
    UserBuyProductService userBuyProductService;
    @Autowired
    WhiteListRepo2 whiteListRepo2;
    @Autowired
    public JavaMailSender emailSender;
    @PostMapping("/indexuser/buyproduct")
    public String addUser(@Validated BuyProductDetail buyProductDetail, BindingResult result, Model model, EntityManager entityManager) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(auth.getName());
            buyProductDetail.setUser(user);
        List<WhiteList>whiteList123= new ArrayList<>();
        for (var i:whiteListRepo.findByUser(userRepository.findByEmail(auth.getName()))) {
            if (i.getAddID()==null){
                whiteList123.add(i);
                i.setAddID(1L);
                whiteListRepo.save(i);
            }
        }
        buyProductDetail.setWhiteLists(whiteList123);
        buyProductDetail.setStatus("Processing");
        buyProductDetail.setStates(0);
        buyProductDetail.setTime(LocalDateTime.now());
        buyProductRepo.save(buyProductDetail);
        int x =0;
        for(var a:buyProductRepo.findByUser(userRepository.findByEmail(auth.getName()))) {
            for (var i: a.getWhiteLists()) {
                x = x + Integer.parseInt(i.getProduct().getPrice()) * i.getQuantity();
            }
            buyProductDetail.setAllPrice(x);
            buyProductRepo.save(buyProductDetail);
            x=0;
        }
        if (buyProductDetail.getPayment().equals("option2")){
            return "redirect:/indexuser/payment/create_payment";
        }

            for (var i : whiteListRepo2.findByUser(userRepository.findByEmail(auth.getName()))) {
                whiteListRepo2.delete(i);
            }
            SimpleMailMessage message = new SimpleMailMessage();
        SimpleMailMessage message2 = new SimpleMailMessage();
            message.setFrom(MyConstant.MY_EMAIL);
            message.setTo(auth.getName());
            message.setSubject("Placed an order successfully");
            message.setText("Hello " + " " + user.getFirstName() + user.getLastName() + "!!!" + " " + "Thank you for your approval," +
                    " we are processing your order and will send it to you as soon as possible"
                    + " " + "You can check it on: " + "http://localhost:8080/indexuser/checkOrder");

        message2.setFrom(MyConstant.MY_EMAIL);
        message2.setTo("hungdoviet456@gmail.com");
        message2.setSubject("New Order!!!!!!!!!");
        message2.setText("You have new order from"+" "+ user.getEmail()+" "+"check it on: "+"http://localhost:8080/indexadmin/buyproduct");
        this.emailSender.send(message,message2);
            return "redirect:/indexuser";
        }
    @GetMapping("/indexadmin/buyproduct")
    public String test123(@Param("keyWord") String keyWord,Model model) {
        if (keyWord!=null){
            List<BuyProductDetail> products = buyProductRepo.seachProduct(keyWord);
            model.addAttribute("t",products);
            return "ProductBuyManager";
        }
        List<BuyProductDetail> buyProductDetails = buyProductRepo.findAll();
        Collections.reverse(buyProductDetails);
        model.addAttribute("t", buyProductDetails);
        List<BuyProductDetail> processOrder = new ArrayList<>();
        List<BuyProductDetail> acceptedOrder = new ArrayList<>();
        for (var i: buyProductRepo.findAll()){
            if (i.getStatus().equals("Accepted")){
                acceptedOrder.add(i);
            } else if (i.getStatus().equals("Processing")) {
                processOrder.add(i);
            }
        }
        Collections.reverse(processOrder);
        Collections.reverse(acceptedOrder);
        model.addAttribute("processOrder",processOrder);
        model.addAttribute("acceptedOrder",acceptedOrder);
        LocalDateTime localDateTime = LocalDateTime.now();
        List<BuyProductDetail> OrderInDay = new ArrayList<>();
        for (var i : buyProductRepo.findAll()){
            if (i.getTime().getMonth()==localDateTime.getMonth()){
                OrderInDay.add(i);
            }
        }
        model.addAttribute("orderInDay",OrderInDay);
        return "ProductBuyManager";
    }
    @GetMapping("/indexadmin/buyproduct/processingOrder")
    public String processingOrder(Model model){
        List<BuyProductDetail> processOrder = new ArrayList<>();
        for (var i: buyProductRepo.findAll()){
            if (i.getStatus().equals("Processing")){
                processOrder.add(i);
            }
        }
        model.addAttribute("t",processOrder);
        return "ProductBuyManager";
    }
    @GetMapping("/indexadmin/buyproduct/AcceptedOrder")
    public String AcceptedOrder(Model model){
        List<BuyProductDetail> acceptedOrder = new ArrayList<>();
        for (var i: buyProductRepo.findAll()){
            if (i.getStatus().equals("Accepted")){
                acceptedOrder.add(i);
            }
        }
        model.addAttribute("t",acceptedOrder);
        return "ProductBuyManager";
    }
    @RequestMapping("/indexuser/checkOrder")
    public String checkOder(Model model){
        int x =0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BuyProductDetail> buyProductDetails = new ArrayList<>();
        for (var i:buyProductRepo.findByUser(userRepository.findByEmail(auth.getName()))) {
            if (i.getStatus().equals("Accepted")||i.getStatus().equals("Processing")||i.getStatus().equals("Sending")){
                buyProductDetails.add(i);
            }
        }
        model.addAttribute("ShippingDetail",buyProductDetails);
        return "checkProduct";
    }
    @GetMapping("/indexadmin/cancelOder/{id}")
    public String cancelOrder(@PathVariable("id") long id){
        BuyProductDetail buyProductDetail = buyProductRepo.findById(id).orElseThrow();
        List<WhiteList> whiteListList = buyProductDetail.getWhiteLists().stream().toList();
            buyProductRepo.deleteById(id);
            for (var i: whiteListList){
                whiteListRepo.delete(i);
            }
        return "redirect:/indexadmin/buyproduct";
    }
    @RequestMapping("/indexadmin/acceptOrder/{id}")
    public String acceptOrder(@PathVariable("id") long id){
        BuyProductDetail buyProductDetail = buyProductRepo.findById(id).orElseThrow();
        buyProductDetail.setStatus("Accepted");
        buyProductRepo.save(buyProductDetail);
        return "redirect:/indexadmin/buyproduct";
    }
    @GetMapping("/indexadmin/sendingOrder/{id}")
    public String sendingOrder(@PathVariable("id") long id){
        BuyProductDetail buyProductDetail = buyProductRepo.findById(id).orElseThrow();
        buyProductDetail.setStatus("Sending");
        buyProductRepo.save(buyProductDetail);
        return "redirect:/indexadmin/buyproduct";
    }
    @GetMapping("/indexadmin/completedOrder/{id}")
    public String CompletedOrder(@PathVariable("id") long id){
        BuyProductDetail buyProductDetail = buyProductRepo.findById(id).orElseThrow();
        buyProductDetail.setStatus("Completed");
        buyProductRepo.save(buyProductDetail);
        return "redirect:/indexadmin/buyproduct";
    }
    @GetMapping("/indexuser/completedOrder/{id}")
    public String CompletedOrder2(@PathVariable("id") long id){
        BuyProductDetail buyProductDetail = buyProductRepo.findById(id).orElseThrow();
        buyProductDetail.setStatus("Completed");
        buyProductRepo.save(buyProductDetail);
        return "redirect:/indexuser";
    }
}