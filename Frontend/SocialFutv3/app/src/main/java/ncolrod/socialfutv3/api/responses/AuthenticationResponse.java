package ncolrod.socialfutv3.api.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representa la respuesta de autenticación que contiene un token.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthenticationResponse {

    /**
     * El token de autenticación.
     */
    private String token;

    /**
     * Obtiene el token de autenticación.
     *
     * @return El token de autenticación.
     */
    public String getToken() {
        return token;
    }
}
