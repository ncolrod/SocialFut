package ncolrod.socialfut.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una solicitud de actualización de estadísticas de un jugador.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsUpdateRequest {

    /**
     * ID del jugador cuyas estadísticas se van a actualizar.
     */
    private int playerId;

    /**
     * Número de goles anotados por el jugador.
     */
    private int goals;

    /**
     * Número de asistencias realizadas por el jugador.
     */
    private int assists;

    /**
     * Número de partidos en los que el jugador ha participado.
     */
    private int participated;
}
