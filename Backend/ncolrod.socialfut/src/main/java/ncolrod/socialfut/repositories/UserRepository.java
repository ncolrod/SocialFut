package ncolrod.socialfut.repositories;

import ncolrod.socialfut.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Repositorio para la entidad User. Proporciona métodos para realizar operaciones CRUD en la base de datos.
 */
@Component
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Método que sirve para buscar un usuario por su email.
     *
     * @param email el email del usuario a buscar
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no se encuentra
     */
    Optional<User> findByEmail(String email);
}
