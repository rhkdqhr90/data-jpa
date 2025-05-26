package stduy.data_jpa.repository;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import stduy.data_jpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        //given
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(saveMember.getId());

        //when

        //then
        assertThat(findMember).isEqualTo(saveMember);
        assertThat(findMember.getUsername()).isEqualTo("memberA");
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() throws Exception{
        //given
        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        //when
        Optional<Member> findMember1 = memberJpaRepository.findById(member1.getId());
        Optional<Member> findMember2 = memberJpaRepository.findById(member2.getId());
        //then

        assertThat(findMember1.get()).isEqualTo(member1);
        assertThat(findMember2.get()).isEqualTo(member2);

        member2.setUsername("member!!!!!");

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all).hasSize(2);
        memberJpaRepository.delete(member1);
        assertThat(memberJpaRepository.count()).isEqualTo(1);

    }

    @Test
    public void findByUsernameAndCreaterThen() throws Exception{
        //given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberJpaRepository.save(member);
        memberJpaRepository.save(member1);
        //when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeCreateThen("AAA", 15);
        //then
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
     }

     @Test
     public void pageing() throws Exception{
         //given
         memberJpaRepository.save(new Member("member1", 10));
         memberJpaRepository.save(new Member("member2", 20));
         memberJpaRepository.save(new Member("member3", 30));
         memberJpaRepository.save(new Member("member4", 30));
         memberJpaRepository.save(new Member("member5", 30));
         memberJpaRepository.save(new Member("member6", 30));

         int age = 30;
         int offset = 1;
         int limit = 3;
         //when
         List<Member> result = memberJpaRepository.findByPage(age, offset, limit);
         long totalCount = memberJpaRepository.totalCount(age);
         //then
         assertThat(result.size()).isEqualTo(limit);
         assertThat(totalCount).isEqualTo(4);
         assertThat(result.get(0).getUsername()).isEqualTo("member5");
         assertThat(result.get(1).getUsername()).isEqualTo("member4");
      }

      @Test
      public void bulkUpdate() throws Exception{
          //given
          memberJpaRepository.save(new Member("member1", 10));
          memberJpaRepository.save(new Member("member2", 20));
          memberJpaRepository.save(new Member("member3", 30));
          memberJpaRepository.save(new Member("member4", 40));
          memberJpaRepository.save(new Member("member5", 30));
          //when
          int resultCount = memberJpaRepository.bulkAgePlus(10);
          //then
          assertThat(resultCount).isEqualTo(5);
       }






}