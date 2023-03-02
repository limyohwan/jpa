package com.example.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.datajpa.dto.MemberDto;
import com.example.datajpa.entity.Member;
import com.example.datajpa.entity.Team;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    
    @PersistenceContext EntityManager em;

    @Autowired MemberQueryRepository memberQueryRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember =memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

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

        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();

        for(Member member : result){
            System.out.println("member = " + member);
            System.out.println("member team = " + member.getTeam().getName());
        }
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");   

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);


    }


    @Test
    public void findByusernamandgreatert(){
        Member member1 = new Member("member1", 10, null);
        Member member2 = new Member("member2", 20, null);   

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByUsernameAndAgeGreaterThan("member2", 15);

        assertThat(findMember.get(0)).isEqualTo(member2);
    }

    @Test
    public void findTop3HelloBy(){
        List<Member> helloby = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery(){
        Member member1 = new Member("member1", 10, null);
        Member member2 = new Member("member2", 20, null);   

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByUsername("member2");

        assertThat(findMember.get(0)).isEqualTo(member2);
    }

    @Test
    public void testQuery(){
        Member member1 = new Member("member1", 10, null);
        Member member2 = new Member("member2", 20, null);   

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findUser("member2", 20);

        assertThat(findMember.get(0)).isEqualTo(member2);
    }

    @Test
    public void findUsernameList(){
        Member member1 = new Member("member1", 10, null);
        Member member2 = new Member("member2", 20, null);   

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> findMember = memberRepository.findUsernameList();

        for(String s : findMember){
            System.out.println(s);
        }
    }


    @Test
    public void joinTest(){
      
        // memberRepository.findJoin();
        memberRepository.findJoinFetch();
    }

    @Test
    public void findMemberDto(){
        Team team = new Team("chelsea0");
        teamRepository.save(team);
        Member member1 = new Member("member1", 10, null);
        member1.setTeam(team);
        memberRepository.save(member1);

        
        List<MemberDto> findMember = memberRepository.findMemberDto();

        for(MemberDto s : findMember){
            System.out.println(s);
        }
    }

    @Test
    public void findByNames(){
        Member member1 = new Member("member1", 10, null);
        Member member2 = new Member("member2", 20, null);   

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByNaems(Arrays.asList("member1", "member2"));

        for(Member s : findMember){
            System.out.println("s = " + s);
        }
    }

    @Test
    public void returntype(){
        Member member1 = new Member("member1", 10, null);
        Member member2 = new Member("member2", 20, null);   

        memberRepository.save(member1);
        memberRepository.save(member2);

        memberRepository.findListByUsername("member1");
        memberRepository.findMemberByUsername("member1");
        memberRepository.findOptionalByUsername("member1");
    }

    @Test
    public void paging(){  
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(10,pageRequest);
        
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        List<Member> content =page.getContent();

        long totalElements = page.getTotalElements();

        for(Member m : content){
            System.out.println("member = " + m);
        }

        System.out.println("total + " + totalElements);

        
    }

    @Test
    public void bulkUpdate(){  
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 19, null));
        memberRepository.save(new Member("member3", 20, null));
        memberRepository.save(new Member("member4", 21, null));
        memberRepository.save(new Member("member5", 40, null));

        int resultCount = memberRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);

        
    }

    @Test
    public void findMemberLazy(){
        
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for(Member m : members){
            System.out.println("member = " + m.getUsername());
            System.out.println("member.teat = " + m.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        Member member1 = memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }
    
    @Test
    public void lock(){
        Member member1 = memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();

        List<Member> findMember = memberRepository.findLockByUsername("member1");

        em.flush();
    }

    @Test
    public void callCustom(){
        List<Member> resut = memberRepository.findMemberCustom();
    }

    @Test
    public void JpaEventBaseEntity() throws InterruptedException{

        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();

        System.out.println("created = " + findMember.getCreatedDate() );
        System.out.println("updated = " + findMember.getLastModifiedDate() );
        System.out.println("updated = " + findMember.getCreatedBy() );
        System.out.println("updated = " + findMember.getLastModifiedBy() );


    }

    @Test
    public void projections(){
        Team teamA = new Team("teamA");

        em.persist(teamA);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        em.persist(member1);
        em.persist(member2);

        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("member1");

        System.out.println(result.get(0).getUsername());
        
    }

    @Test
    public void nativeQuery(){
        Team teamA = new Team("teamA");

        em.persist(teamA);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        em.persist(member1);
        em.persist(member2);

        Page<MemberProjection> member = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        
        for(MemberProjection mp : member.getContent()){
            System.out.println(mp.getUsername() + " | " + mp.getTeamName());

        }
    }
}
