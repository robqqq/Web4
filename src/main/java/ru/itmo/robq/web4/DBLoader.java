package ru.itmo.robq.web4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itmo.robq.web4.data.RoleRepository;
import ru.itmo.robq.web4.model.Role;
import ru.itmo.robq.web4.security.userDetails.UserRole;

import java.util.Arrays;


@Configuration
public class DBLoader {

    @Bean
    CommandLineRunner runRoles (RoleRepository roleRepo) {
        return args -> {
            Arrays.stream(UserRole.values()).forEach(role -> roleRepo.save(new Role(role)));
        };
    }
}
