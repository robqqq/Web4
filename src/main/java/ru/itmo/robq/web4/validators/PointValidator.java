package ru.itmo.robq.web4.validators;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.itmo.robq.web4.exceptions.ValidationException;
import ru.itmo.robq.web4.model.Point;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PointValidator implements Validator<Point>{

    private Validator<Double> xValidator;
    private Validator<Double> yValidator;
    private Validator<Double> rValidator;

    @Autowired
    public PointValidator(
            @Qualifier("xValidator") Validator<Double> xValidator,
            @Qualifier("yValidator") Validator<Double> yValidator,
            @Qualifier("rValidator") Validator<Double> rValidator) {
        this.xValidator = xValidator;
        this.yValidator = yValidator;
        this.rValidator = rValidator;
    }

    @Override
    public void validate(Point value) throws ValidationException{
        xValidator.validate(value.getX());
        yValidator.validate(value.getY());
        rValidator.validate(value.getR());
    }
}
