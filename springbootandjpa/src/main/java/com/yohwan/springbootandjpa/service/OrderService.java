package com.yohwan.springbootandjpa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yohwan.springbootandjpa.domain.Delivery;
import com.yohwan.springbootandjpa.domain.Member;
import com.yohwan.springbootandjpa.domain.Order;
import com.yohwan.springbootandjpa.domain.OrderItem;
import com.yohwan.springbootandjpa.domain.item.Item;
import com.yohwan.springbootandjpa.repository.ItemRepository;
import com.yohwan.springbootandjpa.repository.MemberRepository;
import com.yohwan.springbootandjpa.repository.OrderRepository;
import com.yohwan.springbootandjpa.repository.OrderSearch;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);

        order.cancel();
    }

    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }
}
