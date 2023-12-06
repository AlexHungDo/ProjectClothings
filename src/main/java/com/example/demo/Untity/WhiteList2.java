package com.example.demo.Untity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table
public class WhiteList2 {
    @Id
    private Long whiteListId;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    private int quantity;
}