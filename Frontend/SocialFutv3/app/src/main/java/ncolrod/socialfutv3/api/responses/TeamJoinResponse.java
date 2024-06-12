package ncolrod.socialfutv3.api.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la respuesta al intentar unirse a un equipo.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamJoinResponse {

    /**
     * Indica si la operación de unirse al equipo fue exitosa.
     */
    private boolean successful;
}
