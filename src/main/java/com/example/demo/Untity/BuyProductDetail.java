package com.example.demo.Untity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class BuyProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BuyProID;
    private String FullName;
    private String PhoneNumber;
    private String Address;
    @Column(nullable = true)
    private String Status;
    @Column(nullable = true)
    private String Payment;
    @Column(nullable = true)
    private int States;
    @Column(nullable = true)
    private int allPrice;
    @Column
    private LocalDateTime time;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE)
    @JoinTable(name = "userBuyProduct_Cart",
            joinColumns = @JoinColumn(name = "UserBuyPro_id",
                    referencedColumnName = "BuyProID", nullable = true),
            inverseJoinColumns = @JoinColumn
                    (name = "Cart_id",
                            referencedColumnName = "whiteListId", nullable = true))
    private Collection<WhiteList> whiteLists;
}
