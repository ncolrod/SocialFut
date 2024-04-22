package ncolrod.socialfut.controllers;

import lombok.AllArgsConstructor;
import ncolrod.socialfut.auth.AuthenticationRespose;
import ncolrod.socialfut.auth.RegisterRequest;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.requests.TeamRegisterRequest;
import ncolrod.socialfut.responses.TeamRegisterResponde;
import ncolrod.socialfut.services.TeamService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping(value = "team", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TeamRegisterResponde> register(
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









}
