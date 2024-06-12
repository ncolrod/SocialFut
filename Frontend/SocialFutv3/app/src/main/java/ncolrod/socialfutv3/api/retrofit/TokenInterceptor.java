package ncolrod.socialfutv3.api.retrofit;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * TokenInterceptor es una clase que implementa la interfaz Interceptor de OkHttp.
 * Se utiliza para interceptar solicitudes HTTP y agregar un encabezado de autorización con un token de autenticación.
 */
public class TokenInterceptor implements Interceptor {

    // Instancia única de TokenHolder para obtener el token de autenticación
    private TokenHolder tokenHolder = TokenHolder.getInstance();

    /**
     * Intercepta una solicitud HTTP y agrega el encabezado de autorización si el token está disponible.
     *
     * @param chain la cadena de la solicitud HTTP.
     * @return la respuesta HTTP.
     * @throws IOException si ocurre un error durante la interceptación.
     */
    @Override
    public Response intercept(Chain chain) throws IOException {

        // Obtiene la solicitud actual
        Request request = chain.request();
        System.out.println(request.headers()); // Imprime los encabezados de la solicitud original

        // Verifica si el token está disponible
        if (tokenHolder.getToken() != null) {
            // Si el token está disponible, agrega el encabezado de autorización a la solicitud
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + tokenHolder.getToken())
                    .build();
            System.out.println(request.headers()); // Imprime los encabezados de la solicitud modificada
        }

        // Procede con la solicitud modificada o original
        return chain.proceed(request);
    }
}
