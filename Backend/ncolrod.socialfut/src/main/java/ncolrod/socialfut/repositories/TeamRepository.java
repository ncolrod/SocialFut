package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("SELECT t FROM Team t WHERE LOWER(name)= LOWER(:name)")
    Team findByName(String name);

    //Metodo que nos permitira comprobar las crecendiales de un equipo para que un usuario se una.
    @Query("SELECT t FROM Team t WHERE join_code=:joinCode")
    Team findByJoinCode(String joinCode);

    //Metodo que me devuelve un Team buscandolo por su ID
    Optional<Team> getTeamById(Integer id);

    @Query("SELECT u FROM User u WHERE u.team.id = :teamId")
    List<User> findPlayersByTeamId(int teamId);




}
