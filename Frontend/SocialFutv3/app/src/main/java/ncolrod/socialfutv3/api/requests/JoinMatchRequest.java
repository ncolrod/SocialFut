package ncolrod.socialfutv3.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfutv3.api.models.Team;

@Data
@Builder
@NoArgsConstructor
public class JoinMatchRequest {

    @JsonProperty
    private int matchId;
    @JsonProperty
    private Team awayTeam;
    @JsonProperty
    private boolean isCreated;

    @JsonCreator
    public JoinMatchRequest(@JsonProperty int matchId, @JsonProperty Team awayTeam, @JsonProperty boolean isCreated) {
        this.matchId = matchId;
        this.awayTeam = awayTeam;
        this.isCreated = isCreated;
    }

}
