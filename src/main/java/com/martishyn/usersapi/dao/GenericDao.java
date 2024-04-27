package com.martishyn.usersapi.dao;

public interface GenericDao<T, C> {

    T save(T entity);
}
