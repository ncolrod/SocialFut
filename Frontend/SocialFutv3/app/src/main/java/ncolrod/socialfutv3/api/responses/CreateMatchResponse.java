package ncolrod.socialfutv3.api.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la respuesta de la creación de un partido.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateMatchResponse {

    /**
     * Indica si la creación del partido fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje descriptivo del resultado de la creación del partido.
     */
    private String message;

    /**
     * Identificador del partido creado.
     */
    private int matchId;
}
