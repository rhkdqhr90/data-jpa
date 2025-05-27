package stduy.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.hibernate.dialect.TiDBDialect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import stduy.data_jpa.dto.MemberDto;
import stduy.data_jpa.entity.Member;
import stduy.data_jpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    public void testMember(){
        //given
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);
        Optional<Member> findByMember = memberRepository.findById(member.getId());

        Assertions.assertThat(findByMember.get()).isEqualTo(saveMember);
        Assertions.assertThat(findByMember.get().getUsername()).isEqualTo("memberA");
        Assertions.assertThat(findByMember).isEqualTo(Optional.of(member));

        //when

        //then
     }

    @Test
    public void basicCRUD() throws Exception{
        //given
        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        Optional<Member> findMember1 = memberRepository.findById(member1.getId());
        Optional<Member> findMember2 = memberRepository.findById(member2.getId());
        //then

        assertThat(findMember1.get()).isEqualTo(member1);
        assertThat(findMember2.get()).isEqualTo(member2);



        List<Member> all = memberRepository.findAll();
        assertThat(all).hasSize(2);
        memberRepository.delete(member1);
        assertThat(memberRepository.count()).isEqualTo(1);


    }

    @Test
    public void findByUsernameAndGreaterThen() throws Exception{
        //given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);
        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        //then
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() throws Exception{
        //given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);
        //when
        List<Member> result = memberRepository.findByUser("AAA", 10);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
     }

     @Test
     public void findUserNameList() throws Exception{
         //given
         Member member = new Member("AAA", 10);
         Member member1 = new Member("AAA", 20);
         memberRepository.save(member);
         memberRepository.save(member1);
         //when
         List<String> usernameList = memberRepository.findUsernameList();
         //then
         for(String s: usernameList){
             System.out.println("s =" + s);
         }
      }

      @Test
      public void findMemberDto() throws Exception{
          //given

          Team team = new Team("teamA");
          teamRepository.save(team);
          Member member1 = new Member("AAA", 20);
          member1.changeTeam(team);
          memberRepository.save(member1);


          //when
          List<MemberDto> memberDto = memberRepository.findMemberDto();
          //then
          for(MemberDto m : memberDto){
              System.out.println("dto =" +m);
          }

       }

       @Test
       public void findByNames() throws Exception{
           //given
           Member member = new Member("AAA", 10);
           Member member1 = new Member("AAA", 20);
           memberRepository.save(member);
           memberRepository.save(member1);
           //when
           List<Member> byNames = memberRepository.findByNames(List.of("AAA", "BBB"));
           //then
           for(Member m : byNames){
               System.out.printf("m =" +m);
           }
        }

        @Test
        public void returnType() throws Exception{
            //given
            Member member = new Member("AAA", 10);
            Member member1 = new Member("AAA", 20);
            memberRepository.save(member);
            memberRepository.save(member1);
            //when
            Optional<Member> memberUserById = memberRepository.findMemberUserById(member.getId());
            //then
         }

    @Test
    public void pageing() throws Exception{
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 30));
        memberRepository.save(new Member("member5", 30));
        memberRepository.save(new Member("member6", 30));

        PageRequest username = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        int age =30;
        //when
        Page<Member> page = memberRepository.findByAge(age,username);
        Slice<Member> page1 = memberRepository.findMemberByAge(age, PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")));

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.hasPrevious()).isFalse();
        assertThat(page.isFirst()).isTrue();

    }

    @Test
    public void bulkUpdate() throws Exception{
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 30));

        //when
        int resultCount = memberRepository.bulkAgePlus(10);
        List<Member> all = memberRepository.findAll();
        Member member1 = all.get(0);
        System.out.println(member1);

        //then
        assertThat(resultCount).isEqualTo(5);
    }

    @Test
    public void findMemberLaze() throws Exception{
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member3", 30, teamB));
        em.flush();
        em.clear();

        List<Member> all = memberRepository.findAll();
        for (Member member : all) {
            System.out.println("member = " + member.getUsername());
            System.out.println("team = " + member.getTeam().getClass());
            System.out.println("member Name = "+ member.getTeam().getName());
        }

//        List<Team> all1 = teamRepository.findAll();
//        for(Team team : all1){
//            System.out.println("team = "+ team.getName());
//            System.out.println("member team = " + team.getMembers());
//        }



        //then
     }

     @Test
     public void queryHint() throws Exception{
         //given
         Member member1 = memberRepository.save(new Member("member1", 10));
         em.flush();
         em.clear();
         //when
         Optional<Member> findMember = Optional.ofNullable(memberRepository.findReadOnlyByUsername("member1"));
         findMember.get().setUsername("member111");
         em.flush();

         //then
      }

      @Test
      public void callCustom() throws Exception{
          //given
          List<Member> memberCustom = memberRepository.findMemberCustom();
          //when

          //then
       }
       @Test
       public void JpaEventBaseEntity() throws Exception{
           //given
           Member member = new Member("memberA");
           memberRepository.save(member);

           Thread.sleep(100);
           member.setUsername("memberB");

           em.flush();
           em.clear();
           //when
           memberRepository.findById(member.getId()).get();
           //then
           System.out.println("member = " + member.getCreatedAt());
           System.out.println("member = " + member.getUpdatedAt());
        }

        @Test
        public void specBasic() {
            //given
            Team team  = new Team("teamA");
            em.persist(team);

            Member m1 = new Member("m1", 0, team);
            Member m2 = new Member("m2", 0, team);
            em.persist(m1);
            em.persist(m2);

            em.flush();
            em.clear();

            //when
            Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("team"));
            List<Member> result = memberRepository.findAll(spec);
            //then

         }

         @Test
         public void projections(){
             //given
             Team teamA = new Team("teamA");
             em.persist(teamA);
             Member m1 = new Member("m1", 20, teamA);
             Member m2 = new Member("m2", 0, teamA);
             em.persist(m1);
             em.persist(m2);

             em.flush();
             em.clear();
             //when
//             List<UsernameOnlyDto> result = memberRepository.findProjectionsByUsername("m1",UsernameOnlyDto.class);
              List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1",NestedClosedProjections.class);
             //then
             for(NestedClosedProjections nestedClosedProjections     : result){
                 String username = nestedClosedProjections.getUsername();
                 System.out.println("userNameOnly = " + nestedClosedProjections);
                 String teamName = nestedClosedProjections.getTeam().getName();
                 System.out.println("teamName = " + teamName);

             }
          }













}