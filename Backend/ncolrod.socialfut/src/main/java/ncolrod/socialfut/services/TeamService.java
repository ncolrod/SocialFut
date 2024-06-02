package ncolrod.socialfut.services;

import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.TeamRepository;
import ncolrod.socialfut.repositories.UserRepository;
import ncolrod.socialfut.requests.TeamJoinRequest;
import ncolrod.socialfut.requests.TeamRegisterRequest;
import ncolrod.socialfut.responses.TeamJoinResponse;
import ncolrod.socialfut.responses.TeamRegisterResponse;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public TeamJoinResponse join(TeamJoinRequest request, User user) {
        try {
            String join_code = request.getSafeCode().toString();

            // Verificar si existe un equipo con el nombre y el código seguro proporcionados
            Team optionalTeam = teamRepository.findByJoinCode(join_code);


            if (optionalTeam !=null) {
                // Obtener el equipo con el nombre proporcionado
                Team team = optionalTeam;
                // Asignar el equipo al usuario
                user.setTeam(team);
                // Guardar el usuario actualizado en la base de datos
                userRepository.save(user);

                // Crear una respuesta exitosa
                return new TeamJoinResponse(true);
            } else {
                // Crear una respuesta fallida
                return new TeamJoinResponse(false);
            }

        } catch (Exception e) {
            // Manejar la excepción y devolver una respuesta fallida
            e.printStackTrace();
            return new TeamJoinResponse(false);
        }
    }


    public TeamRegisterResponse register(TeamRegisterRequest request, User user) {
        try {
            // Crea un nuevo equipo con los datos proporcionados en la solicitud
            Team newTeam = new Team();
            newTeam.setName(request.getName());
            newTeam.setLocation(request.getLocation());
            newTeam.setStadium(request.getStadium());
            newTeam.setJoin_code(request.getJoin_code());

            // Guarda el nuevo equipo en la base de datos
            teamRepository.save(newTeam);

            // Asigna automáticamente el rol de administrador al usuario que crea el equipo
            user.setRole(Role.ADMIN);
            user.setTeam(newTeam);
            userRepository.save(user);



            // Devuelve una respuesta de éxito junto con cualquier información adicional
            return new TeamRegisterResponse(true);
        } catch (Exception e) {
            e.printStackTrace();
            // Maneja cualquier excepción que pueda ocurrir durante el proceso de registro del equipo
            return new TeamRegisterResponse(false);
        }
    }

    public Optional<Team> getTeamById(Integer id) {
        return  teamRepository.findById(id);
    }
    public List<User> getTeamPlayers(int teamId) {
        return teamRepository.findPlayersByTeamId(teamId);
    }

    public boolean updateTeam(Team updatedTeam, User user) {
        int teamId = user.getTeam().getId();
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            System.out.println(":::UpdateTeamTask::: --> Equipo encontrado: "+team.getName()+ " con id "+team.getId());
            if (user.getRole() == Role.ADMIN) {
                System.out.println(":::UpdateTeamTask::: --> El usuario "+user.getEmail()+" tiene permisos.");
                team.setName(updatedTeam.getName());
                team.setLocation(updatedTeam.getLocation());
                team.setStadium(updatedTeam.getStadium());
                team.setDescription(updatedTeam.getDescription());
                team.setTeam_color(updatedTeam.getTeam_color());
                teamRepository.save(team);
                return true;
            } else {
                System.out.println(":::UpdateTeamTask::: --> El usuario "+user.getEmail()+" NO tiene permisos.");
                return false;
            }
        } else {
            System.out.println(":::UpdateTeamTask::: --> Equipo NO encontrado.");
            return false;
        }
    }



}

