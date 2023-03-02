package com.example.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.datajpa.entity.Item;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save(){
        Item item = new Item();

        itemRepository.save(item);
    }

    @Test
    public void check(){
        String a = "string";
        String b = "string";

        String c = new String("string");
        String d = c;

        System.out.println("1 = " + (a == b));
        System.out.println("2 = " + (a == c));
        System.out.println("3 = " + (a == d));
        System.out.println("4 = " + (b == c));
        System.out.println("5 = " + (b == d));
        System.out.println("6 = " + (c == d));
    }
}
