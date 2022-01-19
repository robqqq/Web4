package ru.itmo.robq.web4.security.userDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.robq.web4.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CustomUserDetails implements UserDetails {

    @Getter
    private Long id;
    @Getter
    private String username;
    @Getter
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails build(User userEntity) {
        List<GrantedAuthority> authorityList = userEntity.getRoleSet().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().getAuthority()))
                .collect(Collectors.toList());

        return new CustomUserDetails(
                userEntity.getUid(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorityList
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
