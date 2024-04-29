package com.martishyn.usersapi.dao;

import com.martishyn.usersapi.domain.User;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    User save(User entity);

    Optional<User> findById(Long id);

    boolean remove(User user);

    List<User> getAllByDateRange(LocalDate fromDate, LocalDate toDate);
}
