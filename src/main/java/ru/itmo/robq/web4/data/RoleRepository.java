package ru.itmo.robq.web4.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.itmo.robq.web4.model.Role;
import ru.itmo.robq.web4.security.userDetails.UserRole;

@Repository
@RepositoryRestResource(exported = false)
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(UserRole userRole);
}
