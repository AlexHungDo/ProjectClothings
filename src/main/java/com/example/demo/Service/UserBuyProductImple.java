package com.example.demo.Service;

import com.example.demo.Reponsitory.BuyProductRepo;
import com.example.demo.Reponsitory.UserRepository;
import com.example.demo.Untity.BuyProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserBuyProductImple implements UserBuyProductService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BuyProductRepo buyProductRepo;
    @Override
    public List<BuyProductDetail> findUserBuyProductByUserID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return buyProductRepo.findByUser(userRepository.findByEmail(authentication.getName()));
    }
}
