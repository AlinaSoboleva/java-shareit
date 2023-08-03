package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailValidationException;
import ru.practicum.shareit.user.exception.UserIdValidationException;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapperImpl::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapperImpl.toDto(validateUserId(id));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        try {
            User user = userRepository.save(UserMapperImpl.toEntity(userDto));
            return UserMapperImpl.toDto(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("Пользователь не был создан {}", userDto);
            throw new UserEmailValidationException("Пользователь не был создан: " + userDto);
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = validateUserId(userId);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapperImpl.toDto(userRepository.save(user));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User validateUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserIdValidationException(String.format("Пользователь c id: %s не найден", userId)));
    }

}
