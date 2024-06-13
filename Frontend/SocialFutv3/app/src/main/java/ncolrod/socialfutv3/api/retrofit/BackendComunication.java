package ncolrod.socialfutv3.api.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Clase para manejar la comunicación con el backend utilizando Retrofit.
 * Esta clase configura el cliente HTTP y el servicio Retrofit para la comunicación con la API REST.
 */
public class BackendComunication {

    // Base URL del servidor backend
    private final String baseUrl="http://192.168.1.134:8080/";

    // ObjectMapper para la conversión de JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Servicio Retrofit singleton
    private static RetrofitRepository retrofitService;

    /**
     * Constructor privado para configurar el cliente HTTP y el servicio Retrofit.
     * Utiliza OkHttpClient para configurar los tiempos de espera y el interceptor de tokens.
     */
    private BackendComunication() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new TokenInterceptor()) // Añade el interceptor para manejar los tokens
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)) // Utiliza Jackson para convertir JSON
                .build();

        retrofitService = retrofit.create(RetrofitRepository.class);
    }

    /**
     * Conecta y/o devuelve una conexión al servicio Retrofit previamente establecida.
     *
     * @return la instancia del servicio Retrofit.
     */
    public static RetrofitRepository getRetrofitRepository() {
        // Si el servicio Retrofit aún no ha sido inicializado, crea una nueva instancia de BackendComunication
        if (retrofitService == null) {
            new BackendComunication();
        }
        return retrofitService;
    }
}
