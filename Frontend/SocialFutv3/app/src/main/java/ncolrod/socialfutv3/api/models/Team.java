package ncolrod.socialfutv3.api.models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Serializable {

    private int id;
    private String name;
    private String location;
    private String stadium;
    private String join_code;
    private String team_color;
    private String description;
    private User captain;
    private List<User> users;
    private boolean isAvailable;
    //Stats
    private int matchesPlayed;
    private int matchesWon;
    private int lostMatches;
    private int tiedMatches;

}
