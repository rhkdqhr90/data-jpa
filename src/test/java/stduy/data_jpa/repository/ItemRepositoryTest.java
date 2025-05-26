package stduy.data_jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import stduy.data_jpa.entity.Item;


@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save(){
        //given
         Item item = new Item("AA");
         itemRepository.save(item);
        //when

        //then
     }


}