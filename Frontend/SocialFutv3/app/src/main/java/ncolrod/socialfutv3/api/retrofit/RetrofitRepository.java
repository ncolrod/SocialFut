package ncolrod.socialfutv3.api;

import ncolrod.socialfutv3.api.models.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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


    /*
    EQUIPO -> Metodos para buscar y registrar equipos.
    */
    @POST("teams/join")
    @Headers("Content-Type: application/json")
    Call<TeamJoinResponse> join(@Body TeamJoinRequest request);

    @POST("teams/save")
    @Headers("Content-Type: application/json")
    Call<TeamRegisterResponse> teamRegister(@Body TeamRegisterRequest request);


    @GET("users/getuser")
    Call<UserModel> getUser ();



}
