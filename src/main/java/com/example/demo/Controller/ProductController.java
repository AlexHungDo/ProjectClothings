package com.example.demo.Controller;

import com.example.demo.Reponsitory.ProductRepo;
import com.example.demo.Reponsitory.WhiteListRepo;
import com.example.demo.Reponsitory.WhiteListRepo2;
import com.example.demo.Reponsitory.catRepository;
import com.example.demo.Service.ProductService;
import com.example.demo.Untity.Categories;
import com.example.demo.Untity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    catRepository catRepository;
    @Autowired
    WhiteListRepo2 whiteListRepo2;
    @Autowired
    WhiteListRepo whiteListRepo;
   @GetMapping("/indexadmin/product/{catId}")
    public String getProductByCatId(@PathVariable(value = "catId") Long catId, Model model){
        model.addAttribute("listpro",productService.findProductByCatId(catId));
        model.addAttribute("catId",catId);
        return "ac";
    }
    @RequestMapping("indexadmin/{catId}/{proId}")
    public ModelAndView deleteProduct(@PathVariable long catId, @PathVariable long proId){
       Product product = productRepo.findById(proId).orElseThrow();
       if (whiteListRepo.findByProduct(product).getProduct()==product) {
           whiteListRepo2.delete(whiteListRepo2.findByProduct(product));
           whiteListRepo.delete(whiteListRepo.findByProduct(product));
       }
       productRepo.deleteById(proId);
       return new ModelAndView("redirect:/indexadmin/product/"+catId);
    }
    @GetMapping("/indexadmin/{catId}/{proId}/edit")
    public String showUpdateForm(@PathVariable("proId") long proId, Model model,@PathVariable("catId") long catId) {
        Product product = productRepo.findById(proId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + proId));
        model.addAttribute("product",product);
//        model.addAttribute("catId",catId);
        return "updatepro";
    }
    @GetMapping("/indexadmin/{catId}/{proId}/edit2")
    public String showUpdateForm2(@PathVariable("proId") long proId, Model model,@PathVariable("catId") long catId) {
        Product product = productRepo.findById(proId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + proId));
        model.addAttribute("product",product);
//        model.addAttribute("catId",catId);
        return "updatepro2";
    }
    @PostMapping("/indexadmin/update/{catId}/{proId}")
    public String updateUser(@PathVariable("proId") long proId, @Validated Product product,
                             BindingResult result, Model model,@PathVariable(value = "catId") long catId,
    Categories category) {
        product.setProId(proId);
        product.setProName(product.getProName());
        product.setDescription(product.getDescription());
        category= catRepository.findById(catId).orElseThrow(() -> new RuntimeException("ID not found"));
        product.setCategory(category);
        productRepo.save(product);
        return "redirect:/indexadmin/product/"+catId;
    }
    @PostMapping("/indexadmin/update2/{catId}/{proId}")
    public String updateUser2(@PathVariable("proId") long proId, @Validated Product product,
                             BindingResult result, Model model,@PathVariable(value = "catId") long catId,
                             Categories category) {
        product.setProId(proId);
        product.setProName(product.getProName());
        product.setDescription(product.getDescription());
        category= catRepository.findById(catId).orElseThrow(() -> new RuntimeException("ID not found"));
        product.setCategory(category);
        productRepo.save(product);
        return "redirect:/indexadmin";
    }
    @GetMapping("/indexadmin/{catId}/addproduct")
    public String showAddProductForm(Product product,Model model,@PathVariable("catId") long catId) {
       model.addAttribute("caItd",catId);
       return "addpro";
    }
    @PostMapping("/indexadmin/addnewproduct/{catId}")
    public String addProduct(@Validated Product product, BindingResult result, Model model,@PathVariable("catId")
                          long catId, Categories category ) {
       category = catRepository.findById(catId).orElseThrow();
       product.setCategory(category);
        productRepo.save(product);
        return "redirect:/indexadmin/product/"+catId;
    }
    @RequestMapping("/indexadmin/delete/{proId}")
    public ModelAndView deleteProduct2( @PathVariable long proId){
        Product product = productRepo.findById(proId).orElseThrow();
        if (whiteListRepo.findByProduct(product)==null && whiteListRepo2.findByProduct(product)==null){
            productRepo.deleteById(proId);
        }
        else if (whiteListRepo.findByProduct(product).getProduct()==product) {
            whiteListRepo2.delete(whiteListRepo2.findByProduct(product));
            whiteListRepo.delete(whiteListRepo.findByProduct(product));
            productRepo.deleteById(proId);
        }
        return new ModelAndView("redirect:/indexadmin#scrollspyAllProduct");
    }
    @RequestMapping("/indexadmin/addproduct2")
    public String addproduct2(Model model){
       model.addAttribute("listCat",catRepository.findAll());
       return "addproduct2";
    }
    @GetMapping("/indexadmin/{catId}/addproduct/add")
    public String showAddProductForm2(Product product,Model model,@PathVariable("catId") long catId) {
        model.addAttribute("caItd",catId);
        return "addpro2";
    }
    @PostMapping("/indexadmin/addnewproduct/add/{catId}")
    public String addProduct2(@Validated Product product, BindingResult result, Model model,@PathVariable("catId")
    long catId, Categories category ) {
        category = catRepository.findById(catId).orElseThrow();
        product.setCategory(category);
        productRepo.save(product);
        return "redirect:/indexadmin";
    }
    @GetMapping("indexadmin/addCat")
    public String showAddForm(Categories categories){
        return "addCat";
    }
    @PostMapping("/indexadmin/addnewcategory")
    public String addCat(@Validated Categories categories, BindingResult result ) {
        catRepository.save(categories);
        return "redirect:/indexadmin";
    }
    @GetMapping("/indexadmin/deleteCat/{Id}")
    public String DeleteCat(@PathVariable("Id") long id){
       catRepository.deleteById(id);
       return "redirect:/indexadmin";
    }
    @GetMapping("/indexadmin/editCat/{id}")
    public String showUpdateCat(@PathVariable("id") long id, Model model) {
        Categories categories = catRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("cat",categories);
        return "updateCat";
    }
    @PostMapping("/indexadmin/updateCat/{id}")
    public String updateCat(@PathVariable("id") long id, @Validated Categories cat,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            cat.setId(id);
            return "update-user";
        }

        catRepository.save(cat);
        return "redirect:/indexadmin";
    }
}
