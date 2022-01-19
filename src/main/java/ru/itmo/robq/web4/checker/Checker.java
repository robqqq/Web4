package ru.itmo.robq.web4.checker;

public interface Checker<T> {

    boolean check(T value);
}
