package ru.itmo.robq.web4.network;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 2400142614336685323L;

    private String username;
    private String password;
}
