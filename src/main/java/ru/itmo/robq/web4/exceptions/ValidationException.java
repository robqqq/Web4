package ru.itmo.robq.web4.exceptions;

public class ValidationException extends RuntimeException{

    public ValidationException (String msg) {
        super(msg);
    }
}
