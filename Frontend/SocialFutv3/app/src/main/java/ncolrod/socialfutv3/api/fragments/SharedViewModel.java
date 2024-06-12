package ncolrod.socialfutv3.api.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lombok.Getter;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

/**
 * Este SharedViewModel permite a las diferentes partes de la aplicación compartir y observar los mismos datos,
 * garantizando que los cambios se reflejen automáticamente en todas las vistas que estén observando estos datos.
 */
@Getter
public class SharedViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>(); // Usuario actual
    private MutableLiveData<Team> team = new MutableLiveData<>(); // Equipo del usuario actual
    private MutableLiveData<List<Match>> matches = new MutableLiveData<>(); // Partidos disponibles
    private MutableLiveData<List<Match>> matchesPlayed = new MutableLiveData<>(); // Partidos jugados
    private MutableLiveData<List<User>> players = new MutableLiveData<>(); // Jugadores del equipo
    private MutableLiveData<List<User>> homeTeamPlayers = new MutableLiveData<>(); // Jugadores del equipo local
    private MutableLiveData<List<User>> awayTeamPlayers = new MutableLiveData<>(); // Jugadores del equipo visitante
    private MutableLiveData<Match> joinedMatch = new MutableLiveData<>(); // Partido al que se ha unido el usuario

    // Métodos para actualizar los LiveData
    public void setUser(User user) {
        this.user.setValue(user);
    }

    public void setTeam(Team team) {
        this.team.setValue(team);
    }

    public void setMatches(List<Match> matches) {
        this.matches.setValue(matches);
    }

    public void setMatchesPlayed(List<Match> matchesPlayed) {
        this.matchesPlayed.setValue(matchesPlayed);
    }

    public void setPlayers(List<User> players) {
        this.players.setValue(players);
    }

    public void setHomeTeamPlayers(List<User> homeTeamPlayers) {
        this.homeTeamPlayers.setValue(homeTeamPlayers);
    }

    public void setAwayTeamPlayers(List<User> awayTeamPlayers) {
        this.awayTeamPlayers.setValue(awayTeamPlayers);
    }

    public void setJoinedMatch(Match match) {
        this.joinedMatch.setValue(match);
    }

    // Métodos para obtener los LiveData
    public LiveData<User> getUserLiveData() {
        return user;
    }

    public LiveData<Team> getTeamLiveData() {
        return team;
    }

    public LiveData<List<Match>> getMatchesLiveData() {
        return matches;
    }

    public LiveData<List<Match>> getMatchesPlayedLiveData() {
        return matchesPlayed;
    }

    public LiveData<List<User>> getPlayersLiveData() {
        return players;
    }

    public LiveData<List<User>> getHomeTeamPlayersLiveData() {
        return homeTeamPlayers;
    }

    public LiveData<List<User>> getAwayTeamPlayersLiveData() {
        return awayTeamPlayers;
    }

    public LiveData<Match> getJoinedMatchLiveData() {
        return joinedMatch;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Limpiar la memoria
        user = null;
        team = null;
        matches = null;
        matchesPlayed = null;
        players = null;
        homeTeamPlayers = null;
        awayTeamPlayers = null;
        joinedMatch = null;
    }
}
