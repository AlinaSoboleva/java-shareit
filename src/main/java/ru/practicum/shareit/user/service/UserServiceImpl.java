package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            UserDto userDto = UserMapperImpl.toDto(userRepository.getUserById(id));
            return userDto;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        try {
            User user = userRepository.save(UserMapperImpl.toEntity(userDto));
            return UserMapperImpl.toDto(user);
        } catch (Exception e) {
            log.warn("Пользователь не был создан {}", userDto);
            throw new UserEmailValidationException("Пользователь не был создан: " + userDto);
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.getUserById(userId);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapperImpl.toDto(userRepository.save(user));

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.getUserById(userId);
        userRepository.deleteById(userId);
    }

//    @Override
//    public User getUserById(Long userId) {
//        return userRepository.findById(userId).orElseThrow(() ->
//                new UserIdValidationException(String.format("Пользователь c id: %s не найден", userId)));
//    }

}
