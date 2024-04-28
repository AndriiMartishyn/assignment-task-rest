package com.martishyn.usersapi.dao;

import com.martishyn.usersapi.domain.User;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.time.LocalDate;
import java.util.List;

public interface UserDao extends GenericDao<User, Long> {

    List<User> getAllByDateRange(LocalDate fromDate, LocalDate toDate);
}
