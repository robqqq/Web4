package ru.itmo.robq.web4.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class RefreshResponse implements Serializable {

    private static final long serialVersionUID = 2393161204413103782L;

    private String accessToken;
    private String refreshToken;
    private String tokenType;
}
