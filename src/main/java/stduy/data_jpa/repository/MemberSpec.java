package stduy.data_jpa.repository;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import stduy.data_jpa.entity.Member;

public class MemberSpec {
    public static Specification<Member> teamName(final  String teamName){
        return (Specification<Member>) (root, query, cb) -> cb.equal(root.join("team", JoinType.INNER).get("name"), teamName);
    }
    public static Specification<Member> username(final String username){
        return (Specification<Member>) (root, query, cb) -> cb.equal(root.get("username"), username);
    }
}
