package ncolrod.socialfut.services;

import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.TeamRepository;
import ncolrod.socialfut.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private TeamRepository teamRepository;

    public UserService(UserRepository userRepository, TeamRepository teamRepository){
        this.userRepository= userRepository;
        this.teamRepository=teamRepository;
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }




}
