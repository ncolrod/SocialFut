package ncolrod.socialfutv3.api.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para actualizar las estadísticas de un jugador.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsUpdateRequest {

    /** Identificador del jugador cuyas estadísticas se van a actualizar. */
    private int playerId;

    /** Número de goles anotados por el jugador. */
    private int goals;

    /** Número de asistencias realizadas por el jugador. */
    private int assists;

    /** Número de partidos en los que el jugador ha participado. */
    private int participated;
}
