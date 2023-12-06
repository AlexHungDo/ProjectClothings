package com.example.demo.Controller;

import com.example.demo.Reponsitory.*;
import com.example.demo.Service.CatSerVice;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.UserService;
import com.example.demo.Untity.*;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    WhiteListRepo2 whiteListRepo2;
@Autowired
    UserService userService;
    @Autowired
    BuyProductRepo buyProductRepo;
    @Autowired
    CatSerVice catSer;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/")
    public String viewHomePage(Model model, @Param("search") String search){
        model.addAttribute("listCat",catSer.GetAllCategories());
        if (search!=null) {
            List<Product> product = productRepo.findAll();
            product = productRepo.seachProduct(search);
            model.addAttribute("search", search);
            model.addAttribute("listpro", product);
            return "ProductInCategoryNoLogin";
        }
        return "index";
    }
    @GetMapping("/login")
    public String viewLogin(){
        for (var i : userRepository.findAll()){
            if (i.getEmail().equals("hungdoviet456@gmail.com")){
                return "login";
            }
        }
        userService.admin();
        return "login";
    }
    @GetMapping("/indexuser")
    public String ViewIndex(Model model,  @Param("search") String search){
        model.addAttribute("listCat",catSer.GetAllCategories());
        List<Product> product = productRepo.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (search!=null) {
            product = productRepo.seachProduct(search);
            model.addAttribute("search",search);
            model.addAttribute("listpro",product);
            model.addAttribute("cartList",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())));
            model.addAttribute("Items",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())).stream().count());
            int x =0;
            for(var i:whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName()))) {
                x= x+Integer.parseInt(i.getProduct().getPrice())*i.getQuantity();
            }
            model.addAttribute("sumPrice",x);
            return "ProductInCategory";
        }
        model.addAttribute("listpro",product);
        model.addAttribute("cartList",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())));
        model.addAttribute("Items",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())).stream().count());
        int x =0;
        for(var i:whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName()))) {
            x= x+Integer.parseInt(i.getProduct().getPrice())*i.getQuantity();
        }
        model.addAttribute("sumPrice",x);
        User user = userRepository.findByEmail(authentication.getName());
        model.addAttribute("name",user.getLastName());
        return "indexuser";
    }
    @GetMapping("/indexadmin")
    public String AdminView(Model model, @Param("keyWord") String keyWord){
        model.addAttribute("listCat",catSer.GetAllCategories());
        List<Product> product = productRepo.findAll();
        Collections.reverse(product);
        if (keyWord!=null){
        product = productRepo.seachProduct(keyWord);
        }
        model.addAttribute("productList",product);
        model.addAttribute("keyWord",keyWord);
        model.addAttribute("users",userRepository.findAll());
        return "indexadmin";
    }
    @GetMapping("/successPayment")
    public String viewkk(@RequestParam("vnp_ResponseCode") String repo, WhiteList whiteList) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (repo.equals("00")) {
            for (var i : buyProductRepo.findByUser(userRepository.findByEmail(auth.getName()))) {
                if (i.getPayment().equals("option2") && i.getStates() == 0) {
                    i.setStates(1);
                    buyProductRepo.save(i);
                    for (var a : whiteListRepo2.findByUser(userRepository.findByEmail(auth.getName()))) {
                        whiteListRepo2.delete(a);
                    }
                }
            }
            return "kkk";
        }
            for (var i : buyProductRepo.findByUser(userRepository.findByEmail(auth.getName()))) {
                if (i.getPayment().equals("option2") && i.getStates() == 0) {
                    for (var a: i.getWhiteLists()){
                        a.setAddID(null);
                    }
                    buyProductRepo.delete(i);
                }
            }
            return "redirect:/indexuser";


    }
    @GetMapping("/indexuser/productDetail/{id}")
    public String viewProductDetail(@PathVariable("id") long proId, Product product, Model model){{
        product = productRepo.findById(proId).orElseThrow();
        model.addAttribute("product", product);
    return "ProductDetail";
    }
    }
    @GetMapping("/indexuser/c")
    public String abc90(){
        buyProductRepo.deleteAll();
        return "redirect:/indexuser";
    }
}
