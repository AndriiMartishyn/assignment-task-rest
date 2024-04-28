package com.martishyn.usersapi.dao;

import com.martishyn.usersapi.domain.User;

import java.util.Optional;

public interface GenericDao<T, C> {

    T save(T entity);

    Optional<T> findById(Long id);

    boolean remove(User user);
}
