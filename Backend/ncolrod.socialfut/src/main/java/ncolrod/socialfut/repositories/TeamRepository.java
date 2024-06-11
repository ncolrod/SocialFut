package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Team. Proporciona métodos para realizar operaciones CRUD en la base de datos.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    /**
     * Busca un equipo por su nombre, ignorando mayúsculas y minúsculas.
     *
     * @param name el nombre del equipo a buscar
     * @return el equipo si se encuentra
     */
    @Query("SELECT t FROM Team t WHERE LOWER(name) = LOWER(:name)")
    Team findByName(String name);

    /**
     * Busca un equipo por su código de unión.
     * Este método se utiliza para comprobar las credenciales de un equipo para que un usuario se una.
     *
     * @param joinCode el código de unión del equipo
     * @return el equipo si se encuentra
     */
    @Query("SELECT t FROM Team t WHERE join_code = :joinCode")
    Team findByJoinCode(String joinCode);

    /**
     * Devuelve un equipo buscándolo por su ID.
     *
     * @param id el ID del equipo a buscar
     * @return un Optional que contiene el equipo si se encuentra, o vacío si no se encuentra
     */
    Optional<Team> getTeamById(Integer id);

    /**
     * Encuentra todos los jugadores de un equipo por el ID del equipo.
     *
     * @param teamId el ID del equipo
     * @return una lista de usuarios que pertenecen al equipo
     */
    @Query("SELECT u FROM User u WHERE u.team.id = :teamId")
    List<User> findPlayersByTeamId(int teamId);
}
