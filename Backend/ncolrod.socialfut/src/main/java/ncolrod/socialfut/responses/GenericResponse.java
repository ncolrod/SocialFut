package ncolrod.socialfut.responses;

/**
 * Clase que representa una respuesta genérica.
 */
public class GenericResponse {

    /**
     * Indica si la operación fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje que proporciona información adicional sobre la operación.
     */
    private String message;

    /**
     * Constructor que inicializa el éxito y el mensaje de la respuesta.
     *
     * @param success indica si la operación fue exitosa
     * @param message el mensaje que proporciona información adicional
     */
    public GenericResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Obtiene el estado de éxito de la operación.
     *
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Establece el estado de éxito de la operación.
     *
     * @param success true si la operación fue exitosa, false en caso contrario
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Obtiene el mensaje de la operación.
     *
     * @return el mensaje de la operación
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje de la operación.
     *
     * @param message el mensaje de la operación
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
