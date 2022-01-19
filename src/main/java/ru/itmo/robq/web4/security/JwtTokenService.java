package ru.itmo.robq.web4.security;

import org.springframework.security.core.Authentication;
import ru.itmo.robq.web4.security.userDetails.CustomUserDetails;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenService {
    String createToken(CustomUserDetails userDetails);
    Authentication getAuthentication(String token);
    String getUsernameFromToken(String token);
    String resolveToken(HttpServletRequest req);
    boolean validateToken(String token);
}
