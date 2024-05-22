package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Integer> {

    @Query("SELECT f FROM FootballMatch f WHERE f.homeTeam.id != :idTeam")
    List<FootballMatch> listMatchToJoin(int idTeam);


}
