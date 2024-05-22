package ncolrod.socialfutv3.api.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ncolrod.socialfutv3.api.models.Goal;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

/**
 * Clase que representa el ViewModel compartido entre los fragmentos y la actividad principal
 * Esta clase tiene todos los datos que se comparten entre los fragmentos y la actividad principal
 * Los datos que se comparten son:
 * - Usuario
 * - Partidos
 * - Estadísticas del usuario
 */

@Getter
public class SharedViewModel extends ViewModel {

    private MutableLiveData<User> user; // Usuario actual
    private MutableLiveData<Team> team;
    private MutableLiveData<List<Match>> matches; // Partidos del equipo
    private MutableLiveData<List<Goal>> userStatistics; // Estadísticas del usuario
    private MutableLiveData<List<User>> players; // Jugadores del equipo
    private MutableLiveData<Match> joinedMatch; // Partido al que se ha unido el usuario

    public SharedViewModel() {
        user = new MutableLiveData<>();
        team = new MutableLiveData<>();
        matches = new MutableLiveData<>();
        userStatistics = new MutableLiveData<>();
        players = new MutableLiveData<>();
        joinedMatch = new MutableLiveData<>(); // Inicializa la variable
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public void setTeam(Team team) {
        this.team.setValue(team);
    }

    public void setMatches(List<Match> matches) {
        this.matches.setValue(matches);
    }

    public void setUserStatistics(List<Goal> userStatistics) {
        this.userStatistics.setValue(userStatistics);
    }

    public void setPlayers(List<User> players) {
        this.players.setValue(players);
    }

    public void setJoinedMatch(Match match) {
        this.joinedMatch.setValue(match);
    }

    public LiveData<User> getUserLiveData() {
        return user;
    }

    public LiveData<Team> getTeamLiveData() {
        return team;
    }

    public LiveData<List<Match>> getMatchesLiveData() {
        return matches;
    }

    public LiveData<List<Goal>> getUserStatisticsLiveData() {
        return userStatistics;
    }

    public LiveData<List<User>> getPlayersLiveData() {
        return players;
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
        userStatistics = null;
        players = null;
        joinedMatch = null;
    }
}
