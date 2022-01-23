package ru.itmo.robq.web4.security.userDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.robq.web4.data.UserRepository;
import ru.itmo.robq.web4.model.User;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> userEntity = userRepo.findByUsername(username);

        if(!userEntity.isPresent()) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }

        return CustomUserDetails.build(userEntity.get());
    }
}
