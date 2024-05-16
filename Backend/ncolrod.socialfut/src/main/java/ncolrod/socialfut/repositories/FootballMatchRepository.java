package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Integer> {


}
