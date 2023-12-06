package com.example.demo.Untity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table
public class WhiteList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long whiteListId;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    private int quantity;
    @Column(nullable = true)
    private Long AddID;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Collection<BuyProductDetail> buyProductDetails;
}
