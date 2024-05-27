package ncolrod.socialfutv3.api.models;


import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private Timestamp date;
    private String location;
    private String result;
    private String summary;
    private Double pricePerPerson;
    private User creatorUser;
    private boolean isCreated;
    private boolean isFinished;
    private String checkHome;
    private String checkAway;

}
