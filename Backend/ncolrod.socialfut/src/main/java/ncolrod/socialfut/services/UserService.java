package ncolrod.socialfut.services;

import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.TeamRepository;
import ncolrod.socialfut.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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




}
