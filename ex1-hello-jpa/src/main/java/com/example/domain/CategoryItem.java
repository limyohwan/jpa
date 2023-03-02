package com.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CategoryItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "CATEGORY_ITEM_ID")
    private Long id;

    // @Column(name = "ORDER_ID")
    // private Long orderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="CATEGORY_ID")
    private Category category;

    // @Column(name ="ITEM_ID")
    // private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;
}
