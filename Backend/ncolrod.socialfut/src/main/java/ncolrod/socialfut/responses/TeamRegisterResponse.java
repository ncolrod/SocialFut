package ncolrod.socialfut.responses;

import lombok.*;

/**
 * Clase que representa la respuesta al registrar un equipo.
 * Indica si la operación fue exitosa.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamRegisterResponse {

    /**
     * Indica si el registro del equipo fue exitoso.
     */
    private boolean successful;
}
