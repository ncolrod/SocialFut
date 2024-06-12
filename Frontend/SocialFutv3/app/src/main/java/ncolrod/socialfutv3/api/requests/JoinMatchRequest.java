package ncolrod.socialfutv3.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfutv3.api.models.Team;

/**
 * Representa una solicitud para unirse a un partido de fútbol.
 */
@Data
@Builder
@NoArgsConstructor
public class JoinMatchRequest {

    /** Identificador del partido al que se desea unir. */
    @JsonProperty
    private int matchId;

    /** Equipo visitante que se unirá al partido. */
    @JsonProperty
    private Team awayTeam;

    /** Indica si el partido ha sido creado. */
    @JsonProperty
    private boolean isCreated;

    /**
     * Constructor de la clase JoinMatchRequest.
     *
     * @param matchId Identificador del partido al que se desea unir.
     * @param awayTeam Equipo visitante que se unirá al partido.
     * @param isCreated Indica si el partido ha sido creado.
     */
    @JsonCreator
    public JoinMatchRequest(@JsonProperty int matchId, @JsonProperty Team awayTeam, @JsonProperty boolean isCreated) {
        this.matchId = matchId;
        this.awayTeam = awayTeam;
        this.isCreated = isCreated;
    }

}
