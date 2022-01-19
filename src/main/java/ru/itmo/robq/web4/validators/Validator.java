package ru.itmo.robq.web4.validators;

import ru.itmo.robq.web4.exceptions.ValidationException;

public interface Validator<T> {

    void validate(T value) throws ValidationException;
}
