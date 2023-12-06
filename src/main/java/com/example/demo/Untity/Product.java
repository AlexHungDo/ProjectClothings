package com.example.demo.Untity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long proId;
    private String proName;
    private String Description;
    private String price;
    private String pic;
    @ManyToOne(fetch = FetchType.EAGER)
    private Categories category;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<WhiteList> whiteLists=new HashSet<>();
}
