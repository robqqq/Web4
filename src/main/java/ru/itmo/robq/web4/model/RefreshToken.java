package ru.itmo.robq.web4.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "tokens")
public class RefreshToken {

    @Id
    @Column(nullable=false, unique=true)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "uid", unique = true)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RefreshToken that = (RefreshToken) o;
        return refreshToken != null && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
