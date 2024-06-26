package ncolrod.socialfut.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

/**
 * Entidad que representa un partido de fútbol en el sistema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FootballMatch")
public class FootballMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    private String location;
    private String result;
    private String summary;

    private Timestamp date;
    private Double pricePerPerson;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_user_id")
    private User creatorUser;
    private boolean isCreated;
    private boolean isFinished;


    //Contructor para createMatch
    public FootballMatch(Team homeTeam, String location, User creatorUser, Timestamp date, double pricePerPerson) {
        this.homeTeam = homeTeam;
        this.location = location;
        this.creatorUser = creatorUser;
        this.date = date;
        this.pricePerPerson=pricePerPerson;
    }

    //Contructor para joinMatch
    public FootballMatch(Team homeTeam, Team awayTeam, String location, Timestamp date, Double pricePerPerson, User creatorUser, boolean isCreated) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.location = location;
        this.date = date;
        this.pricePerPerson = pricePerPerson;
        this.creatorUser = creatorUser;
        this.isCreated = isCreated;
    }
}
