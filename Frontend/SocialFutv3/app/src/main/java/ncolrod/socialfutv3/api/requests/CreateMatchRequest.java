package ncolrod.socialfutv3.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

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
    private Timestamp date;
    @JsonProperty
    private double pricePerPerson;

    @JsonCreator
    public CreateMatchRequest( @JsonProperty Team homeTeam,@JsonProperty String location, @JsonProperty User creatorUser, @JsonProperty Timestamp date,@JsonProperty Double pricePerPerson) {
        this.homeTeam = homeTeam;
        this.location = location;
        this.creatorUser = creatorUser;
        this.date = date;
        this.pricePerPerson = pricePerPerson;
    }

}
