package com.example.demo.Service;

import com.example.demo.Reponsitory.ProductRepo;
import com.example.demo.Reponsitory.catRepository;
import com.example.demo.Untity.Categories;
import com.example.demo.Untity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProductImple implements ProductService{
    @Autowired
    ProductRepo productRepo;
    @Autowired
    catRepository catRepository;
    @Override
    public List<Product> findProductByCatId(Long id) {
        Categories categories = this.catRepository.findById(id).orElseThrow();
        return this.productRepo.findByCategory(categories);
    }


}
