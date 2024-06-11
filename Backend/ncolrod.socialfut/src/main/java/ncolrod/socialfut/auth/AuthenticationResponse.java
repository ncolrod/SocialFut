package ncolrod.socialfut.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una respuesta de autenticación.
 * Contiene el token JWT generado.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    /**
     * Token JWT generado tras la autenticación.
     */
    private String token;
}
