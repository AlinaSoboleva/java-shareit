package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
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
    public UserDto saveUser(UserDto userDto) {
        return UserMapperImpl.toDto(userRepository.saveUser(UserMapperImpl.toEntity(userDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        return UserMapperImpl.toDto(userRepository.updateUser(userId, UserMapperImpl.toEntity(userDto)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

}
