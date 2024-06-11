package ncolrod.socialfut.responses;

import lombok.*;
import ncolrod.socialfut.entities.FootballMatch;

/**
 * Clase que representa la respuesta al unirse a un partido de fútbol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinMatchResponse {

    /**
     * Indica si unirse al partido fue exitoso.
     */
    private boolean success;

    /**
     * Mensaje que proporciona información adicional sobre la operación de unirse al partido.
     */
    private String message;

    /**
     * Detalles del partido de fútbol al que se unió.
     */
    private FootballMatch footballMatch;

    /**
     * Constructor que inicializa el éxito y los detalles del partido.
     *
     * @param success        indica si unirse al partido fue exitoso
     * @param footballMatch  el partido de fútbol al que se unió
     */
    public JoinMatchResponse(boolean success, FootballMatch footballMatch) {
        this.success = success;
        this.footballMatch = footballMatch;
    }

    /**
     * Constructor que inicializa solo el éxito de la operación.
     *
     * @param success indica si unirse al partido fue exitoso
     */
    public JoinMatchResponse(boolean success) {
        this.success = success;
    }

    /**
     * Constructor que inicializa el éxito y el mensaje de la operación.
     *
     * @param success indica si unirse al partido fue exitoso
     * @param message el mensaje que proporciona información adicional
     */
    public JoinMatchResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
