package com.study.querydsl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;

import static com.querydsl.jpa.JPAExpressions.*;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.querydsl.dto.MemberDto;
import com.study.querydsl.dto.QMemberDto;
import com.study.querydsl.dto.UserDto;
import com.study.querydsl.entity.Member;
import com.study.querydsl.entity.QMember;
import com.study.querydsl.entity.Team;

import static com.study.querydsl.entity.QMember.*;
import static com.study.querydsl.entity.QTeam.*;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
public class QuerydslBasicTest {
    
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
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

    }

    @Test
    public void startJPQL(){
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
        .setParameter("username", "member1")
        .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        
    }

    @Test
    public void startQuertdsl(){
        
        Member findMember = queryFactory
            .select(member)
            .from(member)
            .where(member.username.eq("member1"))
            .fetchOne();

            assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search(){
        Member findMember = queryFactory.
            selectFrom(member)
            .where(
                member.username.eq("member1")
                .and(member.age.eq(10))
            )
            .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchAndParam(){
        Member findMember = queryFactory.
            selectFrom(member)
            .where(
                member.username.eq("member1"),
                member.age.eq(10)
            )
            .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch() {
        List<Member> fetch = queryFactory
        .selectFrom(member)
        .fetch();

        // Member fetchOne = queryFactory
        // .selectFrom(member)
        // .fetchOne();

        Member fetchFirst = queryFactory
        .selectFrom(member)
        .fetchFirst();

        //deprecated
        // QueryResults<Member> results = queryFactory
        // .selectFrom(member)
        // .fetchResults();

        // long total = queryFactory
        // .selectFrom(member)
        // .fetchCount();
        
        long total = queryFactory
        .select(member.count())
        .from(member)
        .fetchOne();
    }

    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(100))
            .orderBy(member.age.desc(), member.username.asc().nullsLast())
            .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1(){
        List<Member> result = queryFactory
        .selectFrom(member)
        .orderBy(member.username.desc())
        .offset(1)
        .limit(2)
        .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void aggregation(){
        List<Tuple> result = queryFactory
            .select(
                member.count(),
                member.age.sum(),
                member.age.avg(),
                member.age.max(),
                member.age.min()
            )
            .from(member)
            .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);   
    }

    @Test
    public void group() throws Exception{
        List<Tuple> result = queryFactory
        .select(team.name, member.age.avg())
        .from(member)
        .join(member.team, team)
        .groupBy(team.name)
        .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    @Test
    public void join(){
        List<Member> result = queryFactory
            .selectFrom(member)
            .leftJoin(member.team, team)
            .where(team.name.eq("teamA"))
            .fetch();

        assertThat(result)
            .extracting("username")
            .containsExactly("member1", "member2");
    }

    @Test
    public void theta_join(){


        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory
            .select(member)
            .from(member,team)
            .where(member.username.eq(team.name))
            .fetch();

        assertThat(result)
        .extracting("username")
        .containsExactly("teamA", "teamB");

    }

    @Test
    public void join_on_filtering(){

        List<Tuple> result = queryFactory
                .select(member,team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for(Tuple t : result){
            System.out.println(t.toString());
        }
    }

    @Test
    public void join_on_no_relation(){


        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = queryFactory
            .select(member,team)
            .from(member)
            .leftJoin(team)
            .on(member.username.eq(team.name))
            .fetch();

        for(Tuple t : result){
            System.out.println(t.toString());
        }

    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchNojoin(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
        .selectFrom(member)
        .where(member.username.eq("member1"))
        .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("패치조인 미적용").isFalse();
    }


    @Test
    public void fetchjoinUse(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
        .selectFrom(member)
        .join(member.team, team).fetchJoin()
        .where(member.username.eq("member1"))
        .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("패치조인 미적용").isTrue();
    }

    @Test
    public void subQuery(){

        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.age.eq(
            select(memberSub.age.max()).from(memberSub)
        ))
        .fetch();

        assertThat(result).extracting("age").containsExactly(40);
    }

    @Test
    public void subQueryGoe(){

        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.age.goe(
            select(memberSub.age.avg()).from(memberSub)
        ))
        .fetch();

        assertThat(result).extracting("age").containsExactly(30, 40);
    }


    @Test
    public void subQueryIn(){

        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.age.in(
            select(memberSub.age).from(memberSub).where(memberSub.age.gt(10))
        ))
        .fetch();

        assertThat(result).extracting("age").containsExactly(20, 30, 40);
    }

    @Test
    public void selectsubQuery(){

        QMember memberSub = new QMember("memberSub");

        List<Tuple> result = queryFactory
        .select(member.username,
            select(memberSub.age.avg()).from(memberSub)
        )
        .from(member)
        .fetch();

        for(Tuple t : result){
            System.out.println(t.toString());
        }
    }

    @Test
    public void basicCase(){
        List<String> result = queryFactory
        .select(member.age
                    .when(10).then("열살")
                    .when(20).then("스무살")
                    .otherwise("기타"))
        .from(member)
        .fetch();

        for(String s : result){
            System.out.println("s = " + s);
        }
    }

    @Test
    public void complexCase(){
        List<String> result = queryFactory
        .select(new CaseBuilder().when(member.age.between(10, 20)).then("0 ~ 20살")
        .when(member.age.between(21, 30)).then("21 ~ 30살")
        .otherwise("기타"))
        .from(member)
        .fetch();

        for(String s : result){
            System.out.println("s = " + s);
        }
    }

    @Test
    public void constant(){
        List<Tuple> result = queryFactory
            .select(member.username, Expressions.constant("A"))
            .from(member)
            .fetch();

        for(Tuple t : result){
            System.out.println("t = " + t);
        }
    }
    
    @Test
    public void concat(){
        List<String> result = queryFactory
            .select(member.username.concat("_").concat(member.age.stringValue()))
            .from(member)
            .fetch();
        for(String s : result){
            System.out.println("s = " + s);
        }
    }

    @Test
    public void simpleProjection(){
        List<String> result = queryFactory.select(member.username)
        .from(member)
        .fetch();

        for(String s : result){
            System.out.println(s);
        }
    }

    @Test
    public void tupleProjection(){
        List<Tuple> result = queryFactory
        .select(member.username, member.age)
        .from(member)
        .fetch();

        for(Tuple t : result){
            String username = t.get(member.username);
            Integer age = t.get(member.age);

            System.out.println("username  = " + username);
            System.out.println("age = " + age);
        }
    }

    @Test
    public void findDtoByJQPL(){
        List<MemberDto> result =em.createQuery("select new com.study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
        .getResultList();

        for(MemberDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void findDtoBySetter(){
        List<MemberDto> result = queryFactory.select(Projections.bean(MemberDto.class, member.username, member.age))
        .from(member)
        .fetch();

        for(MemberDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void findDtoByField(){
        List<MemberDto> result = queryFactory.select(Projections.fields(MemberDto.class, member.username, member.age))
        .from(member)
        .fetch();

        for(MemberDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void findDtoByConstructor(){
        List<MemberDto> result = queryFactory.select(Projections.constructor(MemberDto.class, member.username, member.age))
        .from(member)
        .fetch();

        for(MemberDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void findDtoByUserDto(){
        List<UserDto> result = queryFactory.select(Projections.fields(UserDto.class, member.username.as("name"), member.age))
        .from(member)
        .fetch();

        for(UserDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void findDtoByUserDto2(){
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
            .select(
                Projections.fields(
                    UserDto.class, 
                    //member.username.as("name"), 
                    ExpressionUtils.as(member.username, "name"),
                    ExpressionUtils.as(JPAExpressions.select(memberSub.age.max()).from(memberSub),"age")
                )
            )
            .from(member)
            .fetch();

        for(UserDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void findUserDtoByConstructor(){
        List<UserDto> result = queryFactory.select(Projections.constructor(UserDto.class, member.username, member.age))
        .from(member)
        .fetch();

        for(UserDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void findDtoByQueryPRojection(){
        List<MemberDto> result = queryFactory
            .select(new QMemberDto(member.username, member.age))
            .from(member)
            .fetch();

        for(MemberDto m : result){
            System.out.println(m);
        }
    }

    @Test
    public void dynamicQuery_BooleanBUidler(){
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember1(usernameParam, ageParam);
        
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();
        if(usernameCond != null){
            builder.and(member.username.eq(usernameCond));
        }

        if(ageCond != null){
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
        .selectFrom(member)
        .where(builder)
        .fetch();

    }

    @Test
    public void dynamicQuery_whereparam(){
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember2(usernameParam, ageParam);
        
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {


        return queryFactory.selectFrom(member)
        .where(allEq(usernameCond, ageCond))
        .fetch();
    }

    private BooleanExpression ageEq(Integer ageCond) {
        if(ageCond == null){
            return null;
        }
        return member.age.eq(ageCond);
    }

    private BooleanExpression usernameEq(String usernameCond) {
        if(usernameCond == null){
            return null;
        }
        return member.username.eq(usernameCond);
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond){

        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @Test
    public void bulkUpdate(){
        long count = queryFactory
            .update(member)
            .set(member.username, "비회원")
            .where(member.age.lt(28))
            .execute();

        assertThat(count).isEqualTo(2);

        em.flush();
        em.clear();

        List<Member> result = queryFactory
            .selectFrom(member)
            .fetch();

        for(Member m : result){
            System.out.println(m);
        }
    }

    @Test
    public void bulkAdd(){
        long count = queryFactory
            .update(member)
            .set(member.age, member.age.add(1))
            .execute();
    }

    @Test
    public void bulkDelet(){
        long count = queryFactory
            .delete(member)
            .where(member.age.lt(18))
            .execute();
    }

    @Test
    public void sqlFunction(){
        List<String> result =queryFactory
            .select(
                Expressions.stringTemplate(
                    "function('replace',{0},{1},{2})",
                     member.username,"member","M"))
            .from(member)
            .fetch();


        for(String s : result){
            System.out.println(s);
        }
    }

    @Test
    public void sqlFunction2(){
        List<String> result = queryFactory
            .select(member.username)
            .from(member)
            // .where(member.username.eq(Expressions.stringTemplate("function('lower',{0})", member.username)))
            .where(member.username.eq(member.username.lower()))
            .fetch();

        for(String s : result){
            System.out.println(s);
        }
    }
}
