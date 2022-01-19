package ru.itmo.robq.web4.validators;

import org.springframework.stereotype.Component;
import ru.itmo.robq.web4.exceptions.ValidationException;

@Component("xValidator")
public class XValidator implements Validator<Double> {

    @Override
    public void validate(Double value) throws ValidationException{
        if (value < -2 || value > 2) {
            throw new ValidationException("X value must be in [-2; 2]");
        }
    }
}
