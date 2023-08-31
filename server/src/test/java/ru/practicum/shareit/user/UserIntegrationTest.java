package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        properties = "db.name = test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserIntegrationTest {

    private final EntityManager em;
    private final UserServiceImpl service;

    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("ali@mail.ru");

        userDto2 = new UserDto();
        userDto2.setName("name2");
        userDto2.setEmail("ali2@mail.ru");
    }

    @Test
    void saveUser() {
        service.saveUser(userDto);

        TypedQuery<User> query = em.createQuery("select u from User u where u.email =: email", User.class);

        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }

    @Test
    void getAllUsers() {
        em.persist(UserMapperImpl.toEntity(userDto));
        em.persist(UserMapperImpl.toEntity(userDto2));

        List<UserDto> users = service.getAllUsers();

        TypedQuery<User> query1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        UserDto user = UserMapperImpl.toDto(query1
                .setParameter("email", "ali@mail.ru")
                .getSingleResult());

        TypedQuery<User> query2 = em.createQuery("Select u from User u where u.email = :email", User.class);
        UserDto user2 = UserMapperImpl.toDto(query2
                .setParameter("email", "ali2@mail.ru")
                .getSingleResult());


        assertThat(users, hasItem(user));
        assertThat(users, hasItem(user2));

        assertThat(users, allOf(hasItem(user), hasItem(user2)));
    }
}
