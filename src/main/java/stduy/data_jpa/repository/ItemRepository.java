package stduy.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stduy.data_jpa.entity.Item;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
