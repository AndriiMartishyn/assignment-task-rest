package com.martishyn.usersapi.service.impl;

import com.martishyn.usersapi.domain.User;
import com.martishyn.usersapi.dto.user.UserDto;
import com.martishyn.usersapi.service.PatchValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.beans.FeatureDescriptor;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class DefaultPatchValidationService implements PatchValidationService {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public User patchUserFromDto(UserDto source, User target) {
        BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        String[] nullValues = Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> ObjectUtils.isEmpty(wrappedSource.getPropertyValue(propertyName)) ||
                                                wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
        BeanUtils.copyProperties(source, target, nullValues);
        return target;
    }

    @Override
    public void validateEntity(User user) {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
