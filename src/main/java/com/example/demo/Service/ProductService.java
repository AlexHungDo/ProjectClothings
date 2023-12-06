package com.example.demo.Service;

import com.example.demo.Untity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findProductByCatId(Long catId);
}
