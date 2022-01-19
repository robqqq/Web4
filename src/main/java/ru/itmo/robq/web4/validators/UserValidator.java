package ru.itmo.robq.web4.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.itmo.robq.web4.exceptions.ValidationException;
import ru.itmo.robq.web4.model.User;

@Component
public class UserValidator implements Validator<User>{

    private final Validator<String> usernameValidator;
    private final Validator<String> passwordValidator;

    @Autowired
    public UserValidator(@Qualifier("usernameValidator") Validator<String> usernameValidator,
                         @Qualifier("passwordValidator") Validator<String> passwordValidator) {
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void validate(User value) throws ValidationException {
        usernameValidator.validate(value.getUsername());
        passwordValidator.validate(value.getPassword());
    }
}
