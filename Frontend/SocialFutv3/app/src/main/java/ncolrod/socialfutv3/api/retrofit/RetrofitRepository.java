package ncolrod.socialfutv3.api.retrofit;

import java.util.List;

import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.requests.AuthenticationRequest;
import ncolrod.socialfutv3.api.requests.CreateMatchRequest;
import ncolrod.socialfutv3.api.requests.JoinMatchRequest;
import ncolrod.socialfutv3.api.requests.PlayerStatsUpdateRequest;
import ncolrod.socialfutv3.api.requests.RegisterRequest;
import ncolrod.socialfutv3.api.requests.TeamJoinRequest;
import ncolrod.socialfutv3.api.requests.TeamRegisterRequest;
import ncolrod.socialfutv3.api.responses.AuthenticationRespose;
import ncolrod.socialfutv3.api.responses.CreateMatchResponse;
import ncolrod.socialfutv3.api.responses.GenericResponse;
import ncolrod.socialfutv3.api.responses.JoinMatchResponse;
import ncolrod.socialfutv3.api.responses.TeamJoinResponse;
import ncolrod.socialfutv3.api.responses.TeamRegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitRepository {

    /*
    USUARIO -> Metodos para loguear y registrar usuarios.
     */

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    Call<AuthenticationRespose> login(@Body AuthenticationRequest request);

    @POST("auth/register")
    @Headers("Content-Type: application/json")
    Call<AuthenticationRespose> register(@Body RegisterRequest request);

    //Peticion que devuelve un objeto User
    @GET("users/getuser")
    Call<User> getUser ();

    // Peticion que actualiza la poscion del Usuario
    @PUT("user/position")
    Call<User> updatePosition(@Query("position") String position);

    @PUT("users/updateProfile")
    Call<User> updateUserProfile(@Body User user);


    @DELETE("/users/delete")
    Call<Void> deleteUserProfile(@Query("password") String password);

    @POST("/users/updateStats")
    @Headers("Content-Type: application/json")
    Call<Void> updatePlayerStats(@Body List<PlayerStatsUpdateRequest> playerStats);


    /*
    EQUIPO -> Metodos para buscar y registrar equipos.
    */
    @POST("teams/join")
    @Headers("Content-Type: application/json")
    Call<TeamJoinResponse> join(@Body TeamJoinRequest request);

    @POST("teams/save")
    @Headers("Content-Type: application/json")
    Call<TeamRegisterResponse> teamRegister(@Body TeamRegisterRequest request);

    //Peticion que devuelve un objeto Team
    @GET("teams/getteam")
    Call<Team> getTeam();

    @GET("teams/players")
    Call<List<User>> getPlayers();

    @PUT("teams/update")
    Call<Team> updateTeam(@Body Team updatedTeam);


    /*
    MATCH -> Métodos para crear, unirse y finalizar partidos
     */

    @POST("matches/create")
    @Headers("Content-Type: application/json")
    Call<CreateMatchResponse> createMatch(@Body CreateMatchRequest request);

    @POST("matches/join")
    @Headers("Content-Type: application/json")
    Call<JoinMatchResponse> joinMatch(@Body JoinMatchRequest request);

    @GET("matches/list")
    Call<List<Match>> getMatches();

    @GET("matches/listJoin")
    Call<List<Match>> getJoinMatches();

    @POST("matches/cancel/{matchId}")
    Call<Boolean> cancelMatch(@Path("matchId") int matchId);

    @DELETE("/matches/{matchId}")
    Call<GenericResponse> deleteMatch(@Path("matchId") int matchId);
    @PUT("matches/{id}")
    Call<GenericResponse> updateMatch(@Path("id") int id, @Body CreateMatchRequest request);

    @PUT("matches/result/{matchId}")
    @Headers("Content-Type: application/json")
    Call<Void> updateMatchResult(@Path("matchId") int matchId, @Query("result") String result);

    @GET("matches/{matchId}/homeTeamPlayers")
    Call<List<User>> getHomeTeamPlayers(@Path("matchId") int matchId);
    @GET("matches/{matchId}/awayTeamPlayers")
    Call<List<User>> getAwayTeamPlayers(@Path("matchId") int matchId);












}
