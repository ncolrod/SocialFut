package ncolrod.socialfut.responses;

import lombok.*;

/**
 * Clase que representa la respuesta al intentar unirse a un equipo.
 * Indica si la operación fue exitosa.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamJoinResponse {

    /**
     * Indica si la unión al equipo fue exitosa.
     */
    private boolean successful;
}
