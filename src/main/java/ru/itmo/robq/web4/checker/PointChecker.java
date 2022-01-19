package ru.itmo.robq.web4.checker;

import ru.itmo.robq.web4.model.Point;
import org.springframework.stereotype.Component;

@Component
public class PointChecker implements Checker<Point> {

    @Override
    public boolean check(Point value) {
        return inFirstQuarter (value) && value.getX() <= value.getR() && value.getY()  <= value.getR() / 2 ||
                inSecondQuarter(value) && value.getX() * value.getX() + value.getY() * value.getY() <= value.getR() * value.getR() ||
                inThirdQuarter(value) && value.getY() >= - value.getX() / 2 - value.getR() / 2;
    }

    private boolean inFirstQuarter (Point value) {
        return value.getX() >= 0 && value.getY() >= 0;
    }

    private boolean inSecondQuarter (Point value) {
        return value.getX() <= 0 && value.getY() >= 0;
    }

    private boolean inThirdQuarter (Point value) {
        return value.getX() <= 0 && value.getY() <= 0;
    }

    private boolean inFourthQuarter (Point value) {
        return value.getX() >= 0 && value.getY() <= 0;
    }
}
