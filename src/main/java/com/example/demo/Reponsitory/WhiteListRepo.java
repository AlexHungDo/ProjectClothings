package com.example.demo.Reponsitory;

import com.example.demo.Untity.BuyProductDetail;
import com.example.demo.Untity.Product;
import com.example.demo.Untity.User;
import com.example.demo.Untity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface WhiteListRepo extends JpaRepository<WhiteList,Long> {
    List<WhiteList> findByUser(User user);
    WhiteList findByProduct(Product product);
}
