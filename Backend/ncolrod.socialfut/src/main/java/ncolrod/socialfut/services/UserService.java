package ncolrod.socialfut.services;

import jakarta.transaction.Transactional;
import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.TeamRepository;
import ncolrod.socialfut.repositories.UserRepository;
import ncolrod.socialfut.requests.PlayerStatsUpdateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar usuarios y sus operaciones relacionadas.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Busca un usuario por su email.
     *
     * @param email el email del usuario a buscar
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no se encuentra
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Actualiza el perfil de un usuario.
     *
     * @param userId el ID del usuario a actualizar
     * @param updatedUser el objeto User con los datos actualizados
     * @return el usuario actualizado
     */
    public User updateUserProfile(int userId, User updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Actualizar solo los campos permitidos
        user.setFirstname(updatedUser.getFirstname());
        user.setLastname(updatedUser.getLastname());
        user.setLocation(updatedUser.getLocation());
        user.setPosition(updatedUser.getPosition());

        return userRepository.save(user);
    }

    /**
     * Elimina el perfil de un usuario si la contraseña es correcta.
     *
     * @param user el usuario a eliminar
     * @param password la contraseña del usuario
     * @return true si el perfil fue eliminado, false en caso contrario
     */
    public boolean deleteUserProfile(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    /**
     * Actualiza las estadísticas de los jugadores.
     *
     * @param playerStats una lista de objetos PlayerStatsUpdateRequest con las estadísticas a actualizar
     */
    @Transactional
    public void updatePlayerStats(List<PlayerStatsUpdateRequest> playerStats) {
        for (PlayerStatsUpdateRequest stats : playerStats) {
            User player = userRepository.findById(stats.getPlayerId()).orElse(null);
            if (player != null) {
                int goals = player.getGoals();
                player.setGoals(goals + stats.getGoals());
                int assists = player.getAssists();
                player.setAssists(assists + stats.getAssists());
                int participated = player.getMatchesPlayed();
                player.setMatchesPlayed(participated + stats.getParticipated());
                userRepository.save(player);
            }
        }
    }

    /**
     * Actualiza el rol de un usuario.
     *
     * @param userId el ID del usuario cuyo rol se va a actualizar
     * @param newRole el nuevo rol a asignar
     * @param admin el usuario que realiza la actualización, debe tener rol de ADMIN
     * @return true si el rol fue actualizado, false en caso contrario
     */
    public boolean updateRole(int userId, Role newRole, User admin) {
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Acceso denegado. No tienes permisos para cambiar roles.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setRole(newRole);
        userRepository.save(user);
        return true;
    }
}
