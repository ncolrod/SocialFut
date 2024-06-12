package ncolrod.socialfutv3.api.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud de autenticación con los detalles del usuario.
 */
@Data
@Builder
@NoArgsConstructor
public class AuthenticationRequest {

    /** Correo electrónico del usuario. */
    private String email;

    /** Contraseña del usuario. */
    private String password;

    /**
     * Constructor completo de la clase AuthenticationRequest.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     */
    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
