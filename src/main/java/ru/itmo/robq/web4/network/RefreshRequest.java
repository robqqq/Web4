package ru.itmo.robq.web4.network;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RefreshRequest implements Serializable {

    private static final long serialVersionUID = -8896135404244581209L;

    private String refreshToken;
}
