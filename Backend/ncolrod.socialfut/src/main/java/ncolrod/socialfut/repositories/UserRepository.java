package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {

    /*
    Metodo que sirve para buscar un usuario por su email
     */
    Optional<User> findByEmail(String email);



}
