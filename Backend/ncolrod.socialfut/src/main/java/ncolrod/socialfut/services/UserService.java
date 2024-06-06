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

@Service
public class UserService {

    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder){
        this.userRepository= userRepository;
        this.teamRepository=teamRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    public User updateUserProfile(int userId, User updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Actualizar solo los campos permitidos
        user.setFirstname(updatedUser.getFirstname());
        user.setLastname(updatedUser.getLastname());
        user.setLocation(updatedUser.getLocation());
        user.setPosition(updatedUser.getPosition());

        return userRepository.save(user);
    }

    public boolean deleteUserProfile(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Transactional
    public void updatePlayerStats(List<PlayerStatsUpdateRequest> playerStats) {
        for (PlayerStatsUpdateRequest stats : playerStats) {
            User player = userRepository.findById(stats.getPlayerId()).orElse(null);
            if (player != null) {
                int goals = player.getGoals();
                player.setGoals(goals + stats.getGoals());
                int assists = player.getAssists();
                player.setAssists(assists + stats.getAssists());
                int partipated = player.getMatchesPlayed();
                player.setMatchesPlayed(partipated + stats.getParticipated());
                userRepository.save(player);
            }
        }
    }

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
