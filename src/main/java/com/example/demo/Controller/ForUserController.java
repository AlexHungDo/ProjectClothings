package com.example.demo.Controller;

import com.example.demo.Reponsitory.*;
import com.example.demo.Service.CatSerVice;
import com.example.demo.Service.ProductService;
import com.example.demo.Untity.Product;
import com.example.demo.Untity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ForUserController {
    @Autowired
    WhiteListRepo2 whiteListRepo2;
    @Autowired
    WhiteListRepo whiteListRepo;
    @Autowired
    ProductService productService;
    @Autowired
    CatSerVice catSerVice;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/indexuser/allproduct/{catId}")
    public String getProductByCatId(@PathVariable(value = "catId") Long catId, Model model,
                                    @Param("search") String search){
        List<Product> product = productService.findProductByCatId(catId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (search!=null) {
            product = productRepo.seachProduct(search);
        }
        model.addAttribute("listpro",product);
        model.addAttribute("listCat",catSerVice.GetAllCategories());
        model.addAttribute("cartList",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())));
        model.addAttribute("Items",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())).stream().count());
        model.addAttribute("search",search);
        int x =0;
        for(var i:whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName()))) {
            x= x+Integer.parseInt(i.getProduct().getPrice())*i.getQuantity();
        }
        User user = userRepository.findByEmail(authentication.getName());
        model.addAttribute("sumPrice",x);
        model.addAttribute("name",user.getLastName());
        return "ProductInCategory";
    }
    @GetMapping("/Category/{catId}")
    public String getProductByCatIdNoLogin(@PathVariable(value = "catId") Long catId, Model model,
                                    @Param("search") String search){
        List<Product> product = productService.findProductByCatId(catId);
        if (search!=null) {
            product = productRepo.seachProduct(search);
        }
        model.addAttribute("listpro",product);
        model.addAttribute("listCat",catSerVice.GetAllCategories());
        model.addAttribute("search",search);

        return "ProductInCategoryNoLogin";
    }
}
