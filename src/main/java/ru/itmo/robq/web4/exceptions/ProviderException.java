package ru.itmo.robq.web4.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ProviderException extends RuntimeException{

    private static final long serialVersionUID = 6379048601057421105L;

    @Getter
    private final String message;

    @Getter
    private final HttpStatus httpStatus;
}
