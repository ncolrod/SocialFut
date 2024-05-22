package ncolrod.socialfut.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfut.entities.Team;

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

    public JoinMatchRequest(int matchId, Team awayTeam, boolean isCreated) {
        this.matchId = matchId;
        this.awayTeam = awayTeam;
        this.isCreated = isCreated;
    }
}
