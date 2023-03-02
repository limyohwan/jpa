package com.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem extends BaseEntity  {
    
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    // @Column(name = "ORDER_ID")
    // private Long orderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="ORDER_ID")
    private Order order;

    // @Column(name ="ITEM_ID")
    // private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    private int orderPrice;
    private int count;

    public int getCount() {
        return count;
    }
    public Long getId() {
        return id;
    }

    public int getOrderPrice() {
        return orderPrice;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Item getItem() {
        return item;
    }
    public Order getOrder() {
        return order;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

}
