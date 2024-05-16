package ncolrod.socialfut.controllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.requests.TeamJoinRequest;
import ncolrod.socialfut.requests.TeamRegisterRequest;
import ncolrod.socialfut.responses.TeamJoinResponse;
import ncolrod.socialfut.responses.TeamRegisterResponse;
import ncolrod.socialfut.services.TeamService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /*

    @GetMapping(value = "teams", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AuthenticationRespose> teamRegister(
            @RequestBody TeamRegisterRequest request
    ){
        try{
            return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

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


    @PostMapping(value = "save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TeamRegisterResponse> register(
            @RequestBody TeamRegisterRequest request,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        try{
            User user = (User) userDetails;
            return ResponseEntity.ok(teamService.register(request, user));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "join", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TeamJoinResponse> joinTeam(@RequestBody TeamJoinRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        TeamJoinResponse response = teamService.join(request, (User) userDetails);

        System.out.println(userDetails);
        // Verificar si la operación fue exitosa
        if (response.isSuccesfull()) {
            // Devolver una respuesta exitosa con el código HTTP 200 OK
            return ResponseEntity.ok(response);
        } else {
            // Devolver una respuesta de error con el código HTTP 400 Bad Request
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping(value = "/getteam", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> getTeamByUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            Team userTeam = user.getTeam();
            if (userTeam != null) {
                int teamId = userTeam.getId();
                Optional<Team> team = teamService.getTeamById(teamId);
                System.out.println("TEAM ID: " + teamId);
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
