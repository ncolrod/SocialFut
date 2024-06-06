package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.FootballMatch;
import ncolrod.socialfut.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Integer> {

    @Query("SELECT f FROM FootballMatch f WHERE f.isFinished = false")
    List<FootballMatch> listMatchToJoin(int idTeam);

    @Query("SELECT f FROM FootballMatch f WHERE f.homeTeam.id = :idTeam AND f.isFinished = true")
    List<FootballMatch> listMatchPlayedLocal(int idTeam);

    @Query("SELECT f FROM FootballMatch f WHERE  f.awayTeam.id = :idTeam AND f.isFinished = true")
    List<FootballMatch> listMatchPlayedAway(int idTeam);

    default List<FootballMatch> listMatchPlayed(int idTeam){
        List<FootballMatch> matches = new ArrayList<>(listMatchPlayedLocal(idTeam));
        matches.addAll(listMatchPlayedAway(idTeam));
        return matches;
    }









}
