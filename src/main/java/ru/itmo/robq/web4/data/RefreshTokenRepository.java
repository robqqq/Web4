package ru.itmo.robq.web4.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.itmo.robq.web4.model.RefreshToken;
import ru.itmo.robq.web4.model.User;

import javax.transaction.Transactional;

@Repository
@RepositoryRestResource(exported = false)
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Transactional
    int deleteByUser(User user);

    RefreshToken findByRefreshToken(String refreshToken);
}
