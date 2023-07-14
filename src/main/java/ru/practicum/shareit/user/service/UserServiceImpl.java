package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapperImpl::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapperImpl.toDto(userRepository.getById(id));
    }

    @Override
    public UserDto saveUser(User user) {
        return UserMapperImpl.toDto(userRepository.saveUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        return UserMapperImpl.toDto(userRepository.updateUser(userId, user));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

}
