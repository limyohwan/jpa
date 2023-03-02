package com.yohwan.springbootandjpa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.yohwan.springbootandjpa.domain.Member;
import com.yohwan.springbootandjpa.domain.Order;
import com.yohwan.springbootandjpa.domain.item.Item;
import com.yohwan.springbootandjpa.repository.OrderSearch;
import com.yohwan.springbootandjpa.service.ItemService;
import com.yohwan.springbootandjpa.service.MemberService;
import com.yohwan.springbootandjpa.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping(value="/order")
    public String createFrom(Model model) {
        
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("items", items);
        model.addAttribute("members", members);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam Long memberId, @RequestParam Long itemId, @RequestParam  int count){
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping(value="/orders")
    public String orderList(@ModelAttribute OrderSearch orderSearch, Model model) {
        
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String order(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
