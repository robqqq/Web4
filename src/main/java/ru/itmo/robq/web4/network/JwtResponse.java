package ru.itmo.robq.web4.network;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Long userId;
    private String username;
    private Collection<String> roles;
}
