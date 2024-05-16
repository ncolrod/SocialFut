package ncolrod.socialfut.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    private String location;
    private String result;
    private String summary;
    private Date date;
    private Double pricePerPerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id")
    @JsonIgnore
    private User creatorUser;

    //Contructor para createMatch
    public FootballMatch(Team homeTeam, String location, User creatorUser, Date date, double pricePerPerson) {
        this.homeTeam = homeTeam;
        this.location = location;
        this.creatorUser = creatorUser;
        this.date = date;
        this.pricePerPerson=pricePerPerson;
    }

    //Contructor para joinMatch
    public FootballMatch(Team homeTeam, Team awayTeam, String location, Date date, Double pricePerPerson, User creatorUser) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.location = location;
        this.date = date;
        this.pricePerPerson = pricePerPerson;
        this.creatorUser = creatorUser;
    }
}
