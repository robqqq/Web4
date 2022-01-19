package ru.itmo.robq.web4.validators;

import org.springframework.stereotype.Component;
import ru.itmo.robq.web4.exceptions.ValidationException;

@Component("rValidator")
public class RValidator implements Validator<Double>{

    @Override
    public void validate(Double value) throws ValidationException{
        if (value <= 0 || value > 2) {
            throw new ValidationException("R value must be in (0; 2]");
        }
    }
}
