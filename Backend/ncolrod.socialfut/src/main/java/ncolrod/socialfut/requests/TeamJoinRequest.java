package ncolrod.socialfut.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una solicitud para unirse a un equipo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinRequest {

    /**
     * Código seguro para unirse al equipo.
     */
    private String safeCode;
}
