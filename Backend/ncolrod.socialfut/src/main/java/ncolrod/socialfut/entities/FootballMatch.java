package ncolrod.socialfut.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

import java.sql.Time;
import java.sql.Timestamp;

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

    //Contructor para createMatch
    public FootballMatch(Team homeTeam, String location, User creatorUser, Timestamp date, double pricePerPerson) {
        this.homeTeam = homeTeam;
        this.location = location;
        this.creatorUser = creatorUser;
        this.date = date;
        this.pricePerPerson=pricePerPerson;
    }

    //Contructor para joinMatch
    public FootballMatch(Team homeTeam, Team awayTeam, String location, Timestamp date, Double pricePerPerson, User creatorUser) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.location = location;
        this.date = date;
        this.pricePerPerson = pricePerPerson;
        this.creatorUser = creatorUser;
    }
}
