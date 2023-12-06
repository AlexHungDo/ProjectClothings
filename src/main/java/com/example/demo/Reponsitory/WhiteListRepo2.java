package com.example.demo.Reponsitory;

import com.example.demo.Untity.Product;
import com.example.demo.Untity.User;
import com.example.demo.Untity.WhiteList;
import com.example.demo.Untity.WhiteList2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhiteListRepo2 extends JpaRepository<WhiteList2,Long> {
    WhiteList2 findByProduct(Product product);
    List<WhiteList2> findByUser(User user);
}
