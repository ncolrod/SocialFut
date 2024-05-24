package ncolrod.socialfutv3.api.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lombok.Getter;
import ncolrod.socialfutv3.api.models.Goal;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

@Getter
public class SharedViewModel extends ViewModel {

    private MutableLiveData<User> user; // Usuario actual
    private MutableLiveData<Team> team; // Equipo del usuario actual
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
        joinedMatch = new MutableLiveData<>();
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

    public MutableLiveData<User> getUserLiveData() {
        return user;
    }

    public MutableLiveData<Team> getTeamLiveData() {
        return team;
    }

    public MutableLiveData<List<Match>> getMatchesLiveData() {
        return matches;
    }

    public MutableLiveData<List<Goal>> getUserStatisticsLiveData() {
        return userStatistics;
    }

    public MutableLiveData<List<User>> getPlayersLiveData() {
        return players;
    }

    public MutableLiveData<Match> getJoinedMatchLiveData() {
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
