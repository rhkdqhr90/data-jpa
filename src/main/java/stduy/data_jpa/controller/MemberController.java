package stduy.data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import stduy.data_jpa.dto.MemberDto;
import stduy.data_jpa.entity.Member;
import stduy.data_jpa.repository.MemberRepository;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init(){
      for(int i = 0; i < 100; i++){
          memberRepository.save(new Member("user"+i,i));
      }
    }

    @GetMapping("members/{id}")
    public String findMemberById(@PathVariable("id")Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("members1/{id}")
    public String findMemberById2(@PathVariable("id")Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }
}
