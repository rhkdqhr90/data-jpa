package stduy.data_jpa.dto;

import lombok.Data;
import stduy.data_jpa.entity.Member;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
    public MemberDto(Member member){
        this(member.getId(),member.getUsername(),member.getTeam() == null ? null : member.getTeam().getName());
    }
}
