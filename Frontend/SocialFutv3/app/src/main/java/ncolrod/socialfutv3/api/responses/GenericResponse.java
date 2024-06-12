package ncolrod.socialfutv3.api.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representa una respuesta genérica de la API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GenericResponse {

    /**
     * Indica si la operación fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje descriptivo del resultado de la operación.
     */
    private String message;

}
