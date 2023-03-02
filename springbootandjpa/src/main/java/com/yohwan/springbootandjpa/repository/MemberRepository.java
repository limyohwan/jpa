package com.yohwan.springbootandjpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yohwan.springbootandjpa.domain.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);
    
    
}
