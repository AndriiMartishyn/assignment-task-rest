package com.martishyn.usersapi.dao;

import java.util.Optional;

public interface GenericDao<T, C> {

    T save(T entity);

    Optional<T> findById(Long id);
}
