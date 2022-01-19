package ru.itmo.robq.web4.validators;

import org.springframework.stereotype.Component;
import ru.itmo.robq.web4.exceptions.ValidationException;

@Component
public class UsernameValidator implements Validator<String> {

    @Override
    public void validate(String value) throws ValidationException {
        if (value == null) {
            throw new ValidationException("Username is not set");
        }
        if (value.length() < 4) {
            throw new ValidationException("Username must be at least 4 characters long");
        }
        if (value.length() > 20) {
            throw new ValidationException("Username must be not more than 20 characters long");
        }
        if (value.charAt(0) >= '0' && value.charAt(0) <= '9'){
            throw new ValidationException("Username must starts with a letter");
        }
    }
}
