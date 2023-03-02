package com.study.querydsl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.study.querydsl.dto.MemberSearchCondition;
import com.study.querydsl.dto.MemberTeamDto;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition conditon);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition conditon, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition conditon, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex2(MemberSearchCondition conditon, Pageable pageable);

}
