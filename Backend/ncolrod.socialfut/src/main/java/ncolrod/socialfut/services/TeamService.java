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

/**
 * Servicio para gestionar las operaciones relacionadas con los equipos.
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    /**
     * Busca un equipo por su nombre.
     *
     * @param name el nombre del equipo a buscar
     * @return el equipo encontrado
     */
    public Team findTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    /**
     * Permite a un usuario unirse a un equipo utilizando un código de unión.
     *
     * @param request la solicitud de unión al equipo
     * @param user el usuario que se une al equipo
     * @return una respuesta indicando si la unión fue exitosa
     */
    public TeamJoinResponse join(TeamJoinRequest request, User user) {
        try {
            String join_code = request.getSafeCode().toString();

            // Verificamos si existe un equipo con el código seguro proporcionado
            Team optionalTeam = teamRepository.findByJoinCode(join_code);

            if (optionalTeam != null) {
                // Asignamos el equipo al usuario
                user.setTeam(optionalTeam);
                // Guardamos el usuario actualizado en la base de datos
                userRepository.save(user);

                // Creamos una respuesta exitosa
                return new TeamJoinResponse(true);
            } else {
                // Creamos una respuesta fallida
                return new TeamJoinResponse(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejamos la excepción y devolver una respuesta fallida
            return new TeamJoinResponse(false);
        }
    }

    /**
     * Registra un nuevo equipo y asigna automáticamente el rol de administrador al usuario que lo crea.
     *
     * @param request la solicitud de registro del equipo
     * @param user el usuario que crea el equipo
     * @return una respuesta indicando si el registro fue exitoso
     */
    public TeamRegisterResponse register(TeamRegisterRequest request, User user) {
        try {
            // Creamos un nuevo equipo con los datos proporcionados en la solicitud
            Team newTeam = new Team();
            newTeam.setName(request.getName());
            newTeam.setLocation(request.getLocation());
            newTeam.setStadium(request.getStadium());
            newTeam.setJoin_code(request.getJoin_code());
            // Guardamos el nuevo equipo en la base de datos
            teamRepository.save(newTeam);
            // Asignamos automáticamente el rol de administrador al usuario que crea el equipo
            user.setRole(Role.ADMIN);
            user.setTeam(newTeam);
            userRepository.save(user);
            // Devolvemos una respuesta de éxito
            return new TeamRegisterResponse(true);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejamos cualquier excepción que pueda ocurrir durante el registro del equipo
            return new TeamRegisterResponse(false);
        }
    }

    /**
     * Obtiene un equipo por su ID.
     *
     * @param id el ID del equipo a buscar
     * @return un Optional que contiene el equipo si se encuentra, o vacío si no se encuentra
     */
    public Optional<Team> getTeamById(Integer id) {
        return teamRepository.findById(id);
    }

    /**
     * Obtiene los jugadores de un equipo por el ID del equipo.
     *
     * @param teamId el ID del equipo
     * @return una lista de jugadores que pertenecen al equipo
     */
    public List<User> getTeamPlayers(int teamId) {
        return teamRepository.findPlayersByTeamId(teamId);
    }

    /**
     * Actualiza la información de un equipo.
     *
     * @param updatedTeam el equipo actualizado
     * @param user el usuario que realiza la actualización, debe tener rol de administrador
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean updateTeam(Team updatedTeam, User user) {
        int teamId = user.getTeam().getId();
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            System.out.println(":::UpdateTeamTask::: --> Equipo encontrado: " + team.getName() + " con id " + team.getId());
            if (user.getRole() == Role.ADMIN) {
                System.out.println(":::UpdateTeamTask::: --> El usuario " + user.getEmail() + " tiene permisos.");
                team.setName(updatedTeam.getName());
                team.setLocation(updatedTeam.getLocation());
                team.setStadium(updatedTeam.getStadium());
                team.setDescription(updatedTeam.getDescription());
                team.setTeam_color(updatedTeam.getTeam_color());
                teamRepository.save(team);
                return true;
            } else {
                System.out.println(":::UpdateTeamTask::: --> El usuario " + user.getEmail() + " NO tiene permisos.");
                return false;
            }
        } else {
            System.out.println(":::UpdateTeamTask::: --> Equipo NO encontrado.");
            return false;
        }
    }
}
