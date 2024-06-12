package ncolrod.socialfutv3.api.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para unirse a un equipo utilizando un código seguro.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinRequest {

    /**
     * El código seguro utilizado para unirse al equipo.
     */
    private String safeCode;
}
