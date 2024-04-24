package ncolrod.socialfutv3.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitRepository {

    /*
    USUARIO -> Metodos para buscar y registrar usuarios. Uso en el login y register.
     */

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    Call<AuthenticationRespose> login(@Body AuthenticationRequest request);

    @POST("auth/register")
    @Headers("Content-Type: application/json")
    Call<AuthenticationRespose> register(@Body RegisterRequest request);



}
