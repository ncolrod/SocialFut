package ncolrod.socialfut.controllers;

import lombok.AllArgsConstructor;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.requests.TeamJoinRequest;
import ncolrod.socialfut.requests.TeamRegisterRequest;
import ncolrod.socialfut.responses.TeamJoinResponse;
import ncolrod.socialfut.responses.TeamRegisterResponse;
import ncolrod.socialfut.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los equipos.
 */
@RestController
@RequestMapping("/teams")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /**
     * Endpoint para obtener un equipo por su nombre.
     *
     * @param name el nombre del equipo a buscar
     * @return una respuesta con los detalles del equipo o un error si no se encuentra
     */
    @GetMapping(value = "teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> getTeamByName(@RequestParam("name") String name) {
        try {
            Team team = teamService.findTeamByName(name);
            if (team != null) {
                return ResponseEntity.ok(team);
            } else {
                // Equipo no encontrado
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para registrar un nuevo equipo.
     *
     * @param request la solicitud de registro del equipo
     * @param userDetails los detalles del usuario autenticado
     * @return una respuesta indicando si el registro fue exitoso o si hubo un error
     */
    @PostMapping(value = "save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TeamRegisterResponse> register(
            @RequestBody TeamRegisterRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            User user = (User) userDetails;
            return ResponseEntity.ok(teamService.register(request, user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para unirse a un equipo.
     *
     * @param request la solicitud de unión al equipo
     * @param userDetails los detalles del usuario autenticado
     * @return una respuesta indicando si la unión fue exitosa o si hubo un error
     */
    @PostMapping(value = "join", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TeamJoinResponse> joinTeam(@RequestBody TeamJoinRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        TeamJoinResponse response = teamService.join(request, (User) userDetails);
        // Verificamos si la operación fue exitosa
        if (response.isSuccessful()) {
            // Devolvemos una respuesta exitosa con el código HTTP 200 OK
            return ResponseEntity.ok(response);
        } else {
            // Devolvemos una respuesta de error con el código HTTP 400 Bad Request
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para obtener el equipo del usuario autenticado.
     *
     * @param userDetails los detalles del usuario autenticado
     * @return una respuesta con los detalles del equipo o un error si no se encuentra
     */
    @GetMapping(value = "/getteam", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> getTeamByUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            Team userTeam = user.getTeam();
            if (userTeam != null) {
                int teamId = userTeam.getId();
                Optional<Team> team = teamService.getTeamById(teamId);
                if (team.isPresent()) {
                    return ResponseEntity.ok(team.get());
                } else {
                    // Equipo no encontrado
                    return ResponseEntity.notFound().build();
                }
            } else {
                // El usuario no tiene un equipo asociado
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para actualizar la información de un equipo.
     *
     * @param updatedTeam los nuevos datos del equipo
     * @param userDetails los detalles del usuario autenticado
     * @return una respuesta indicando si la actualización fue exitosa o si hubo un error
     */
    @PutMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Team> updateTeam(@RequestBody Team updatedTeam, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            if (teamService.updateTeam(updatedTeam, user)) {
                return ResponseEntity.ok(updatedTeam);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para obtener los jugadores de un equipo.
     *
     * @param userDetails los detalles del usuario autenticado
     * @return una respuesta con la lista de jugadores del equipo o un error si no se encuentran
     */
    @GetMapping("/players")
    public ResponseEntity<List<User>> getTeamPlayers(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Team userTeam = user.getTeam();
        int teamId = userTeam.getId();
        List<User> players = teamService.getTeamPlayers(teamId);
        if (players != null) {
            return ResponseEntity.ok(players);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
