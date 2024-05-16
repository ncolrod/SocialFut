package ncolrod.socialfut.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
public class CreateMatchRequest {

    @JsonProperty
    private Team homeTeam;
    @JsonProperty
    private String location;
    @JsonProperty
    private User creatorUser;
    @JsonProperty
    private Date date;
    @JsonProperty
    private double pricePerPerson;

    public CreateMatchRequest(Team homeTeam, String location, User creatorUser, Date date, double pricePerPerson) {
        this.homeTeam = homeTeam;
        this.location = location;
        this.creatorUser = creatorUser;
        this.date = date;
        this.pricePerPerson = pricePerPerson;
    }

}
