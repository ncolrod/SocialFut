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

    public JoinMatchRequest(int matchId, Team awayTeam) {
        this.matchId = matchId;
        this.awayTeam = awayTeam;
    }
}
