package ncolrod.socialfut.services;

import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.TeamRepository;
import ncolrod.socialfut.repositories.UserRepository;
import ncolrod.socialfut.requests.TeamRegisterRequest;
import ncolrod.socialfut.responses.TeamRegisterResponde;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team findTeamByName(String name) {
        // Implementa la lógica para buscar un equipo por su nombre en el repositorio
        // Por ejemplo, puedes llamar a un método del repositorio que busque el equipo por su nombre
        return teamRepository.findByName(name);
    }


    public TeamRegisterResponde register(TeamRegisterRequest request, User user) {
        try {
            // Crea un nuevo equipo con los datos proporcionados en la solicitud
            Team newTeam = new Team();
            newTeam.setName(request.getName());
            newTeam.setLocation(request.getLocation());
            newTeam.setStadium(request.getStadium());
            newTeam.setJoin_code(request.getJoin_code());
            newTeam.setTeam_color(request.getTeam_color());

            // Asigna automáticamente el rol de administrador al usuario que crea el equipo
            user.setRole(Role.ADMIN);
            userRepository.save(user);

            // Guarda el nuevo equipo en la base de datos
            Team savedTeam = teamRepository.save(newTeam);

            // Devuelve una respuesta de éxito junto con cualquier información adicional
            return new TeamRegisterResponde("Team registered successfully", savedTeam);
        } catch (Exception e) {
            e.printStackTrace();
            // Maneja cualquier excepción que pueda ocurrir durante el proceso de registro del equipo
            return new TeamRegisterResponde();
        }
    }
}

