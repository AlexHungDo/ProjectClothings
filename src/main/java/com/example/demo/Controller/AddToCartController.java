package com.example.demo.Controller;

import com.example.demo.Myconstant.MyConstant;
import com.example.demo.Reponsitory.*;
import com.example.demo.Untity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AddToCartController {
    @Autowired
    WhiteListRepo2 whiteListRepo2;

    @Autowired
    ProductRepo productRepo;
    @Autowired
    WhiteListRepo whiteListRepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BuyProductRepo buyProductRepo;

    @GetMapping("/indexuser/addToCart/{proId}/{catId}")
    public String addtoCart(@PathVariable("proId") long proId, WhiteList whiteList, WhiteList2 whiteList2, @PathVariable("catId") long catId,
                            Product product, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (var i : whiteListRepo.findAll()) {
            if (i.getProduct().getProId()==proId && i.getUser()==userRepository.findByEmail(authentication.getName())) {
                        i.setQuantity(i.getQuantity());
                        whiteListRepo.save(i);
            }
        }
        for (var a : whiteListRepo2.findAll()) {
            if (a.getProduct().getProId()==proId && a.getUser()==userRepository.findByEmail(authentication.getName())
                    ) {
                a.setQuantity(a.getQuantity());
                whiteListRepo2.save(a);
                return "redirect:/indexuser/allproduct/" + catId;
            }
        }
            product = productRepo.findById(proId).orElseThrow();
            User user = userRepository.findByEmail(authentication.getName());
            whiteList.setUser(user);
            whiteList.setQuantity(1);
            whiteList.setProduct(product);
            whiteListRepo.save(whiteList);
            whiteList2.setWhiteListId(whiteList.getWhiteListId());
            whiteList2.setProduct(product);
            whiteList2.setUser(user);
            whiteList2.setQuantity(1);
            whiteListRepo2.save(whiteList2);
            return "redirect:/indexuser/allproduct/" + catId;
        }
    @GetMapping("/indexuser/BuyProductNow/{proId}")
    public String BuyProductNow(@PathVariable("proId") long proId, WhiteList whiteList, WhiteList2 whiteList2,
                            Product product, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (var i : whiteListRepo.findAll()) {
            if (i.getProduct().getProId()==proId && i.getUser()==userRepository.findByEmail(authentication.getName())) {
                i.setQuantity(i.getQuantity());
                whiteListRepo.save(i);
            }
        }
        for (var a : whiteListRepo2.findAll()) {
            if (a.getProduct().getProId()==proId && a.getUser()==userRepository.findByEmail(authentication.getName())
            ) {
                a.setQuantity(a.getQuantity());
                whiteListRepo2.save(a);
                return "redirect:/indexuser/abc";
            }
        }
        product = productRepo.findById(proId).orElseThrow();
        User user = userRepository.findByEmail(authentication.getName());
        whiteList.setUser(user);
        whiteList.setQuantity(1);
        whiteList.setProduct(product);
        whiteListRepo.save(whiteList);
        whiteList2.setWhiteListId(whiteList.getWhiteListId());
        whiteList2.setProduct(product);
        whiteList2.setUser(user);
        whiteList2.setQuantity(1);
        whiteListRepo2.save(whiteList2);
        return "redirect:/indexuser/abc";
    }


    @GetMapping("/indexuser/abc")
    public String viewCard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("cardItems",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())));
        model.addAttribute("Items",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())).stream().count()+" Items in your-cart");
        int x =0;
        for(var i:whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName()))) {
            x = x+Integer.parseInt(i.getProduct().getPrice())*i.getQuantity();
        }
        model.addAttribute("sumPrice",x);
        return "whitelist";
    }
    @RequestMapping("/indexuser/delete/{whiteListId}")
    public ModelAndView deleteProductCart(@PathVariable long whiteListId){
        whiteListRepo.deleteById(whiteListId);
        whiteListRepo2.deleteById(whiteListId);
        return new ModelAndView("redirect:/indexuser/abc");
    }
    @RequestMapping("/indexuser/minusQuantity/{cartId}")
    public ModelAndView minusProductQuantity(@PathVariable long cartId){
        WhiteList2 whiteList2 = whiteListRepo2.findById(cartId).orElseThrow();
        WhiteList whiteList = whiteListRepo.findById(cartId).orElseThrow();
        whiteList2.setQuantity(whiteList2.getQuantity()-1);
        whiteList.setQuantity(whiteList2.getQuantity()-1);
        whiteListRepo2.save(whiteList2);
        whiteListRepo.save(whiteList);

        if (whiteList2.getQuantity()<=0){
            whiteListRepo2.delete(whiteList2);
        }
        if (whiteList.getQuantity()<=0){
            whiteListRepo.delete(whiteList);
        }
        return new ModelAndView("redirect:/indexuser/abc");
    }
    @RequestMapping("/indexuser/quantity/{cartId}")
    public ModelAndView ProductQuantity(@Validated WhiteList2 cart, BindingResult result,@PathVariable long cartId) {
        WhiteList2 whiteList2 = whiteListRepo2.findById(cartId).orElseThrow();
        WhiteList whiteList = whiteListRepo.findById(cartId).orElseThrow();
        whiteList2.setQuantity(cart.getQuantity());
        whiteList.setQuantity(cart.getQuantity());
        whiteListRepo2.save(whiteList2);
        if (whiteList2.getQuantity() == 0) {
            whiteListRepo2.delete(whiteList2);
            whiteListRepo.delete(whiteList);
        }
        return new ModelAndView("redirect:/indexuser/abc");
    }
    @RequestMapping("/indexuser/addQuantity/{cartId}")
    public ModelAndView addProductQuantity(@PathVariable long cartId){
        WhiteList2 whiteList2 = whiteListRepo2.findById(cartId).orElseThrow();
        WhiteList whiteList = whiteListRepo.findById(cartId).orElseThrow();
        whiteList2.setQuantity(whiteList2.getQuantity()+1);
        whiteList.setQuantity(whiteList.getQuantity()+1);
        whiteListRepo2.save(whiteList2);
        whiteListRepo.save(whiteList);
        return new ModelAndView("redirect:/indexuser/abc");
    }
    @RequestMapping("/indexuser/buyProduct")
    public String buyProduct(Model model, BuyProductDetail buyProductDetail){
        int x =0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for(var i:whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName()))) {
            x= x+Integer.parseInt(i.getProduct().getPrice())*i.getQuantity();
        }
        model.addAttribute("cardItems",whiteListRepo2.findByUser(userRepository.findByEmail(authentication.getName())));
        model.addAttribute("sumPrice",x+"$");
        return "BuyProduct";
    }

}
