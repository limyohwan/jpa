package com.study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
public class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void testEntity(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> result = em.createQuery("select m from Member m left join fetch m.team", Member.class).getResultList();
        
        for(Member m : result){
            System.out.println("m = " +  m.getUsername());
            System.out.println("m.team = " +  m.getTeam().getName());
        }
    }
}
