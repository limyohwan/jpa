package com.example.datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.datajpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    
    private final EntityManager em;

    List<Member> findAllMembers(){

        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}