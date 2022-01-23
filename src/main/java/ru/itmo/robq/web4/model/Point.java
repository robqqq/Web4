package ru.itmo.robq.web4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "points")
public class Point implements Serializable {

    private static final long serialVersionUID = -3185873326989486872L;

    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private Double r;

    @Column(nullable = false)
    private Boolean result;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_uid", referencedColumnName = "uid")
    private User user;

    public Point (Double x, Double y, Double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Point point = (Point) o;
        return id != null && Objects.equals(id, point.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
