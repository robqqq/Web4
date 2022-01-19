package ru.itmo.robq.web4.validators;

import org.springframework.stereotype.Component;
import ru.itmo.robq.web4.exceptions.ValidationException;

@Component
public class PasswordValidator implements Validator<String>{

    @Override
    public void validate(String value) throws ValidationException {
        if (value == null) {
            throw new ValidationException("Password is not set");
        }
        if (value.length() < 6) {
            throw new ValidationException("Password must be at least 4 characters long");
        }
        if (value.length() > 20) {
            throw new ValidationException("Password must be not more than 20 characters long");
        }
    }
}
