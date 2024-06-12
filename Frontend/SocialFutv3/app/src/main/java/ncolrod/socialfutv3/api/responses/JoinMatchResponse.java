package ncolrod.socialfutv3.api.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ncolrod.socialfutv3.api.models.Match;

/**
 * Representa la respuesta al intentar unirse a un partido.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JoinMatchResponse {

    /**
     * Indica si la operación de unirse al partido fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje descriptivo del resultado de la operación.
     */
    private String message;

    /**
     * El partido de fútbol al que se ha intentado unir el usuario.
     */
    private Match footballMatch;
}
