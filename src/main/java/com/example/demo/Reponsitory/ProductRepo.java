package com.example.demo.Reponsitory;

import com.example.demo.Untity.Categories;
import com.example.demo.Untity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByCategory(Categories categories);
    @Query("SELECT c FROM Product c WHERE c.proName LIKE %?1%")
    List<Product> seachProduct(String search);
}
