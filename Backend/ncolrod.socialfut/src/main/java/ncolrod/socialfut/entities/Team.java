package ncolrod.socialfut.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Team", uniqueConstraints = {@UniqueConstraint(name = "UQ_name", columnNames = "name")})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private int id;
    private String name;
    private String location;
    private String stadium;
    private String join_code;
    private String team_color;
    private String description;

    @OneToMany(mappedBy = "team")
    private List<User> users;
}

