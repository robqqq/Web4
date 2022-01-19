package ru.itmo.robq.web4.network;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class RegisterRequest implements Serializable {

    private static final long serialVersionUID = -6646280871132369752L;

    private String username;
    private String password;
    private List<String> roles;
}
