package stduy.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stduy.data_jpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
