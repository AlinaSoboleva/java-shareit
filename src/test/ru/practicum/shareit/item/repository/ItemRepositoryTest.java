package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Item item;

    private Item item2;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setName("al");
        user.setEmail("mail@mail.ru");

        userRepository.save(user);

        item = new Item();
        item.setOwner(user);
        item.setAvailable(true);
        item.setDescription("desc");
        item.setName("name");

        item2 = new Item();
        item2.setOwner(user);
        item2.setAvailable(true);
        item2.setDescription("desc2");
        item2.setName("name2");

        itemRepository.save(item);
        itemRepository.save(item2);
    }

    @Test
    void findAllItemsByOwnerId() {

        List<Item> expected = itemRepository.findAllItemsByOwnerId(user.getId(), PageRequest.of(0, 10));

        assertThat(expected, hasSize(2));
        assertThat(expected.get(0), equalTo(item));
        assertThat(expected.get(1), equalTo(item2));
    }

    @Test
    void findBySearch() {
        List<Item> expected = itemRepository.findBySearch("name2", PageRequest.of(0, 10));

        assertThat(expected, hasSize(1));
        assertThat(expected.get(0), equalTo(item2));
    }

    @Test
    void getItemById() {
        Item expected = itemRepository.getItemById(item.getId());

        assertThat(item, equalTo(expected));
    }

    @Test
    void getItemById_whenItemNotFount() {
        Long itemId = 0l;
        Exception exception = assertThrows(ItemIdValidationException.class, () ->
                itemRepository.getItemById(itemId));

        assertThat("Предмет с id: " + itemId + " не найдена", equalTo(exception.getMessage()));
    }
}