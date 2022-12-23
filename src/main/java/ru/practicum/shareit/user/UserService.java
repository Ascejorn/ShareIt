package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public UserDto create(UserDto userDto) {
        ifEmailExistThrowingException(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.add(user));
    }

    public UserDto getById(Long userId) {
        return UserMapper.toUserDto(getByIdWithExceptionCheck(userId));
    }

    public Collection<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto update(UserDto userDto, Long userId) {
        User user = getByIdWithExceptionCheck(userId);
        if (!user.getEmail().equals(userDto.getEmail())) {
            ifEmailExistThrowingException(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        userStorage.add(user);
        return UserMapper.toUserDto(user);
    }

    public void delete(Long userId) {
        userStorage.delete(userId);
    }

    private void ifEmailExistThrowingException(String email) {
        if (userStorage.isEmailExist(email)) {
            throw new UserAlreadyExistsException(email);
        }
    }

    private User getByIdWithExceptionCheck(Long userId) {
        Optional<User> user = userStorage.getById(userId);
        if (user.isEmpty()) {
            throw new UserAlreadyExistsException("User by id " + userId);
        }
        return user.get();
    }
}
