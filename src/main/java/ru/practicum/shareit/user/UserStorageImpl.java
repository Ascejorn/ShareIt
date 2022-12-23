package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users;
    private long idGenerator;

    @Override
    public User add(User user) {
        if (user.getId() == null) {
            user.setId(++idGenerator);
            users.put(idGenerator, user);
        } else {
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return (users.containsKey(userId))
                ? Optional.of(users.get(userId))
                : Optional.empty();
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public boolean isEmailExist(String email) {
        return users.values().stream()
                .map(User::getEmail)
                .anyMatch(e -> e.equals(email));
    }
}