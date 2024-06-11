package ncolrod.socialfut.responses;

import lombok.*;

/**
 * Clase que representa la respuesta al crear un partido de fútbol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchResponse {

    /**
     * Indica si la creación del partido fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje que proporciona información adicional sobre la creación del partido.
     */
    private String message;

    /**
     * ID del partido creado.
     */
    private int matchId;

    /**
     * Constructor que inicializa el éxito de la creación del partido.
     *
     * @param success indica si la creación del partido fue exitosa
     */
    public CreateMatchResponse(boolean success) {
        this.success = success;
    }

    /**
     * Constructor que inicializa el éxito y el mensaje de la creación del partido.
     *
     * @param success indica si la creación del partido fue exitosa
     * @param message el mensaje que proporciona información adicional
     */
    public CreateMatchResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
