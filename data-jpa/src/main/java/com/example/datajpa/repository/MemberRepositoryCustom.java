package com.example.datajpa.repository;

import java.util.List;

import com.example.datajpa.entity.Member;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
