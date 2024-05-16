package ncolrod.socialfutv3.api.models;


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
    private String location;
    private String result;
    private String summary;
    private User creatorUser;

}
