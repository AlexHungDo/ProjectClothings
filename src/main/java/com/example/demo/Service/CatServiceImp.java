package com.example.demo.Service;

import com.example.demo.Reponsitory.catRepository;
import com.example.demo.Untity.Categories;
import com.example.demo.Untity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CatServiceImp implements CatSerVice {
    @Autowired
    catRepository catRepo;
    @Override
    public List<Categories> GetAllCategories() {
        return catRepo.findAll();
    }


}
