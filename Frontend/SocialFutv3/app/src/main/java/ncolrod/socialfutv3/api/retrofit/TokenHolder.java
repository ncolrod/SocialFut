package ncolrod.socialfutv3.api.retrofit;

/**
 * TokenHolder es una clase singleton que se utiliza para almacenar y gestionar el token de autenticación
 * del usuario en toda la aplicación.
 */
public class TokenHolder {

    // Instancia única de la clase (singleton)
    private static TokenHolder instance = new TokenHolder();

    // Variable para almacenar el token de autenticación
    private String token = null;

    /**
     * Obtiene el token de autenticación actual.
     *
     * @return el token de autenticación.
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token de autenticación.
     *
     * @param token el nuevo token de autenticación.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Obtiene la instancia única de TokenHolder.
     *
     * @return la instancia única de TokenHolder.
     */
    public static TokenHolder getInstance() {
        return instance;
    }
}
