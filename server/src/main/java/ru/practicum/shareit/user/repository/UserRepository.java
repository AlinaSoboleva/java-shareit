package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserIdValidationException;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User getUserById(Long userId) {
        User user = findById(userId).orElseThrow(() ->
                new UserIdValidationException(String.format("Пользователь c id: %s не найден", userId)));
        return user;
    }
}
