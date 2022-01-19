package ru.itmo.robq.web4.security.filter;

import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itmo.robq.web4.exceptions.ProviderException;
import ru.itmo.robq.web4.security.JwtTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        try {
            if (token != null && jwtProvider.validateToken(token)) {
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (ProviderException e) {
            SecurityContextHolder.clearContext();
            response.resetBuffer();
            response.setStatus(e.getHttpStatus().value());
            response.setHeader("Content-Type", "application/json");
            PrintWriter out = response.getWriter();
            JSONObject object = new JSONObject();
            object.appendField("message", e.getMessage());
            out.write(object.toJSONString());
            return;
        } catch (Exception e) {
            response.resetBuffer();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Content-Type", "application/json");
            PrintWriter out = response.getWriter();
            JSONObject object = new JSONObject();
            object.appendField("message", e.getMessage());
            out.write(object.toJSONString());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
