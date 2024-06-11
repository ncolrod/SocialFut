package ncolrod.socialfut.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


/**
 * Entidad que representa un equipo en el sistema.
 */
@Builder
@Data
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
    @JsonIgnore
    private String join_code;
    private String team_color;
    private String description;
    private boolean isAvailable;
    //Stats
    private int matchesPlayed;
    private int matchesWon;
    private int lostMatches;
    private int tiedMatches;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<User> users;
}

