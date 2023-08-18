package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;


    @Test
    void findAllByItemId() {
        User user = new User();
        user.setName("al");
        user.setEmail("mail@mail.ru");

        userRepository.save(user);

        Item item = new Item();
        item.setOwner(user);
        item.setAvailable(true);
        item.setDescription("desc");
        item.setName("name");
        itemRepository.save(item);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("text");
        comment.setCreated(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setAuthor(user);
        comment2.setItem(item);
        comment2.setText("text2");
        comment2.setCreated(LocalDateTime.now());

        repository.save(comment);
        repository.save(comment2);

        List<Comment> comments = repository.findAllByItemId(item.getId());

        assertThat(comments, hasSize(2));
        assertThat(comment, equalTo(comments.get(0)));
        assertThat(comment2, equalTo(comments.get(1)));
    }
}