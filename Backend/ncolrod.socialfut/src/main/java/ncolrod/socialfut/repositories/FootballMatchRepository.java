package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la entidad FootballMatch. Proporciona métodos para realizar operaciones CRUD en la base de datos.
 */
@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Integer> {

    /**
     * Lista los partidos que no han terminado y que un equipo visitante puede unirse.
     *
     * @param idTeam el ID del equipo
     * @return una lista de partidos que no han terminado
     */
    @Query("SELECT f FROM FootballMatch f WHERE f.isFinished = false")
    List<FootballMatch> listMatchToJoin(int idTeam);

    /**
     * Lista los partidos finalizados jugados en casa.
     *
     * @param idTeam el ID del equipo
     * @return una lista de partidos locales que han terminado
     */
    @Query("SELECT f FROM FootballMatch f WHERE f.homeTeam.id = :idTeam AND f.isFinished = true")
    List<FootballMatch> listMatchPlayedLocal(int idTeam);

    /**
     * Lista los partidos finalizados jugados fuera.
     *
     * @param idTeam el ID del equipo
     * @return una lista de partidos visitantes
     */
    @Query("SELECT f FROM FootballMatch f WHERE f.awayTeam.id = :idTeam AND f.isFinished = true")
    List<FootballMatch> listMatchPlayedAway(int idTeam);

    /**
     * Lista todos los partidos jugados por un equipo (tanto en casa como fuera) que han terminado.
     *
     * @param idTeam el ID del equipo
     * @return una lista de todos los partidos jugados que han terminado
     */
    default List<FootballMatch> listMatchPlayed(int idTeam) {
        List<FootballMatch> matches = new ArrayList<>(listMatchPlayedLocal(idTeam));
        matches.addAll(listMatchPlayedAway(idTeam));
        return matches;
    }
}
