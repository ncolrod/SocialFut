package ncolrod.socialfut.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una solicitud de autenticación.
 * Contiene el email y la contraseña del usuario.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    /**
     * Email del usuario.
     */
    private String email;

    /**
     * Contraseña del usuario.
     */
    private String password;
}
