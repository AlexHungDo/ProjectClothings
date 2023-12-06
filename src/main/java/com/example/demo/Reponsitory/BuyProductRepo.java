package com.example.demo.Reponsitory;

import com.example.demo.Untity.BuyProductDetail;
import com.example.demo.Untity.Product;
import com.example.demo.Untity.User;
import com.example.demo.Untity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BuyProductRepo extends JpaRepository<BuyProductDetail,Long> {
    List<BuyProductDetail> findByUser(User user);
    @Query("SELECT c FROM BuyProductDetail c WHERE c.PhoneNumber LIKE %?1%")
    List<BuyProductDetail> seachProduct(String keyWord);
}
