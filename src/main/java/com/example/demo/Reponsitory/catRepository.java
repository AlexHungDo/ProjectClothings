package com.example.demo.Reponsitory;

import com.example.demo.Untity.Categories;
import com.example.demo.Untity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface catRepository extends JpaRepository<Categories,Long> {
}
