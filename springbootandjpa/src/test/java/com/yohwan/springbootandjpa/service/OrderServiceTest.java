package com.yohwan.springbootandjpa.service;

import static org.junit.Assert.*;
import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yohwan.springbootandjpa.domain.Address;
import com.yohwan.springbootandjpa.domain.Member;
import com.yohwan.springbootandjpa.domain.Order;
import com.yohwan.springbootandjpa.domain.OrderStatus;
import com.yohwan.springbootandjpa.domain.item.Book;
import com.yohwan.springbootandjpa.exception.NotEnoughStockException;
import com.yohwan.springbootandjpa.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        //given
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "gangga", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("new JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(),book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("item order status is order", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("order item count is collect", 1, getOrder.getOrderItems().size());
        assertEquals("order price is item price multiple quantity", 10000*orderCount, getOrder.getTotalPrice());
        assertEquals("must minus order's stockquantity", 8, book.getStockQuantity());
    }

    @Test
    public void 주문취소() {
        //given
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "gangga", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("new JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //when

        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals("state is CANCEL", OrderStatus.CANCEL, order.getStatus());
        assertEquals("must plus canceled order's quantity ", 10, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과() {

        //given
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "gangga", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("new JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 11;

        //when
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail(" 에러나야함");

    }
}