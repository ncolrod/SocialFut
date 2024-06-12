package ncolrod.socialfutv3.api.responses;

import lombok.*;

/**
 * Representa la respuesta al intentar registrar un equipo.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamRegisterResponse {

    /**
     * Indica si la operación de registro del equipo fue exitosa.
     */
    private boolean successful;
}
