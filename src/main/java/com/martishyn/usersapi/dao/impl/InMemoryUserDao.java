package com.martishyn.usersapi.dao.impl;

import com.martishyn.usersapi.dao.UserDao;
import com.martishyn.usersapi.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> userMap = new HashMap<>();
    private final AtomicLong atomicInteger = new AtomicLong(1L);


    @Override
    public User save(User entity) {
        if (entity.getId() == null) {
            entity.setId(atomicInteger.getAndIncrement());
        }
        userMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public boolean remove(User user) {
        return userMap.remove(user.getId()) != null;
    }

    @Override
    public List<User> getAllByDateRange(LocalDate fromDate, LocalDate toDate) {
        return userMap.values()
                .stream()
                .filter(user -> user.getBirthDate().isAfter(fromDate) && user.getBirthDate().isBefore(toDate))
                .toList();
    }
}
