package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("SELECT t FROM Team t WHERE LOWER(name)= LOWER(:name)")
    Team findByName(String name);
}
