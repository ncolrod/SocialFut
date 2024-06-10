package ncolrod.socialfutv3.api.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BackendComunication {

    private final String baseUrl="http://192.168.1.134:8080/";
    private final ObjectMapper objectMapper= new ObjectMapper();
    private static RetrofitRepository retrofitService;

    private BackendComunication(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new TokenInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        retrofitService=retrofit.create(RetrofitRepository.class);
    }


    /**
     * Conecta y/o devuelve una conexión a la base de datos previamente establecida.
     *
     * @return la conexión de la base de datos.
     * @throws SQLException devuelve una excepción si no se puede obtener la conexión.
     */
    public static RetrofitRepository getRetrofitRepository() {

        if (retrofitService==null){
            new BackendComunication();
        }
        return retrofitService;
    }



}
