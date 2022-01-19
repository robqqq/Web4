package ru.itmo.robq.web4.validators;

import org.springframework.stereotype.Component;
import ru.itmo.robq.web4.exceptions.ValidationException;

@Component("yValidator")
public class YValidator implements Validator<Double> {

    @Override
    public void validate(Double value) throws ValidationException{
        if (value <= -5 || value >= 5) {
            throw new ValidationException("Y value must be in (-5; 5)");
        }
    }
}
