package ncolrod.socialfut.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private String join_code;
    private String team_color;
    private String description;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<User> users;
}

