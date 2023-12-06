package com.example.demo.Service;

import com.example.demo.Untity.BuyProductDetail;

import java.util.List;

public interface UserBuyProductService {
    List<BuyProductDetail> findUserBuyProductByUserID();
}
