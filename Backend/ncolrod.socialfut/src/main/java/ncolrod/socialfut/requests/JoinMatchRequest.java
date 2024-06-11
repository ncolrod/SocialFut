package ncolrod.socialfut.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfut.entities.Team;

/**
 * Clase que representa una solicitud para unirse a un partido de fútbol.
 */
@Data
@Builder
@NoArgsConstructor
public class JoinMatchRequest {

    /**
     * El ID del partido al que se desea unir.
     */
    @JsonProperty
    private int matchId;

    /**
     * El equipo visitante que se unirá al partido.
     */
    @JsonProperty
    private Team awayTeam;

    /**
     * Indica si el partido ha sido creado.
     */
    @JsonProperty
    private boolean isCreated;

    /**
     * Constructor que inicializa todos los campos de la solicitud.
     *
     * @param matchId   el ID del partido al que se desea unir
     * @param awayTeam  el equipo visitante que se unirá al partido
     * @param isCreated indica si el partido ha sido creado
     */
    public JoinMatchRequest(int matchId, Team awayTeam, boolean isCreated) {
        this.matchId = matchId;
        this.awayTeam = awayTeam;
        this.isCreated = isCreated;
    }
}
