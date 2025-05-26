package stduy.data_jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of ={"id","username","age"})
@NamedQuery(name = "Member.findByUsernameAndAge", query = "select m from Member m where m.username = :username and m.age > :age")
public class Member extends BaseEntity{


    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;



    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
       if(team != null){
           changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    /**
     * 연관관계 의 메소드
     * change team
     * @param team
     */
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
