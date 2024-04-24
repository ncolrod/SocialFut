package ncolrod.socialfut.data;

import ncolrod.socialfut.api.AuthenticationRequest;
import ncolrod.socialfut.api.AuthenticationRespose;
import ncolrod.socialfut.api.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitRepository {

    /*
    USUARIO -> Metodos para buscar y registrar usuarios. Uso en el login y register.
     */

    @POST("auth/login")
    Call<AuthenticationRespose> login(@Body AuthenticationRequest request);

    @POST("auth/register")
    Call<AuthenticationRespose> register(@Body RegisterRequest request);



}
