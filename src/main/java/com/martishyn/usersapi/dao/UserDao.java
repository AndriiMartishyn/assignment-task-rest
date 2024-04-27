package com.martishyn.usersapi.dao;

import com.martishyn.usersapi.domain.User;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public interface UserDao extends GenericDao<User, Long> {
}
