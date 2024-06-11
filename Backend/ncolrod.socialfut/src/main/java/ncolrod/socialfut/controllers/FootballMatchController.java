package ncolrod.socialfut.controllers;

import jakarta.persistence.EntityNotFoundException;
import ncolrod.socialfut.entities.FootballMatch;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.requests.CreateMatchRequest;
import ncolrod.socialfut.requests.JoinMatchRequest;
import ncolrod.socialfut.responses.CreateMatchResponse;
import ncolrod.socialfut.responses.GenericResponse;
import ncolrod.socialfut.responses.JoinMatchResponse;
import ncolrod.socialfut.services.FootballMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los partidos de fútbol.
 */
@RestController
@RequestMapping("/matches")
public class FootballMatchController {

    private final FootballMatchService footballMatchService;

    public FootballMatchController(FootballMatchService footballMatchService) {
        this.footballMatchService = footballMatchService;
    }

    /**
     * Endpoint para crear un partido de fútbol.
     *
     * @param request      la solicitud de creación del partido
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la creación fue exitosa o si hubo un error
     */
    @PostMapping("/create")
    public ResponseEntity<CreateMatchResponse> createMatch(@RequestBody CreateMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        CreateMatchResponse response = footballMatchService.createMatch(request, userDetails);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Endpoint para unirse a un partido de fútbol.
     *
     * @param request      la solicitud de unión al partido
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la unión fue exitosa o si hubo un error
     */
    @PostMapping("/join")
    public ResponseEntity<JoinMatchResponse> joinMatch(@RequestBody JoinMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        JoinMatchResponse response = footballMatchService.joinMatch(request, userDetails);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Endpoint para obtener todos los partidos de fútbol.
     *
     * @return una lista de todos los partidos de fútbol o un error si ocurre un problema
     * @throws Exception si ocurre un error al recuperar los partidos
     */
    @GetMapping("/list")
    public ResponseEntity<List<FootballMatch>> getAllMatches() throws Exception {
        List<FootballMatch> matches = footballMatchService.findAllMatches();
        if (matches != null) {
            return ResponseEntity.ok(matches);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para obtener los partidos jugados por un equipo.
     *
     * @param userDetails los detalles del usuario autenticado
     * @return una lista de partidos jugados por el equipo o un error si ocurre un problema
     * @throws Exception si ocurre un error al recuperar los partidos
     */
    @GetMapping("/listPlayed")
    public ResponseEntity<List<FootballMatch>> getMatchesPlayed(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        List<FootballMatch> matches = footballMatchService.listMatchesPlayed(userDetails);
        if (matches != null) {
            return ResponseEntity.ok(matches);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para obtener los partidos a los que un equipo puede unirse.
     *
     * @param userDetails los detalles del usuario autenticado
     * @return una lista de partidos a los que el equipo puede unirse o un error si ocurre un problema
     * @throws Exception si ocurre un error al recuperar los partidos
     */
    @GetMapping("/listJoin")
    public ResponseEntity<List<FootballMatch>> getJoinMatches(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        List<FootballMatch> matches = footballMatchService.listJoinMatches(userDetails);
        if (matches != null) {
            return ResponseEntity.ok(matches);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para cancelar un partido de fútbol.
     *
     * @param matchId      el ID del partido a cancelar
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la cancelación fue exitosa o si hubo un error
     */
    @PostMapping("/cancel/{matchId}")
    public ResponseEntity<Boolean> cancelMatch(@PathVariable int matchId, @AuthenticationPrincipal UserDetails userDetails) {
        boolean result = footballMatchService.cancelMatch(matchId, userDetails);
        if (result) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
    }

    /**
     * Endpoint para eliminar un partido de fútbol.
     *
     * @param matchId      el ID del partido a eliminar
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la eliminación fue exitosa o si hubo un error
     */
    @DeleteMapping("/{matchId}")
    public ResponseEntity<GenericResponse> deleteMatch(@PathVariable int matchId, @AuthenticationPrincipal UserDetails userDetails) {
        boolean success = footballMatchService.deleteMatch(matchId, userDetails);
        if (success) {
            return ResponseEntity.ok(new GenericResponse(true, "Match deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericResponse(false, "Failed to delete match"));
        }
    }

    /**
     * Endpoint para actualizar un partido de fútbol.
     *
     * @param matchId      el ID del partido a actualizar
     * @param request      la solicitud de actualización del partido
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la actualización fue exitosa o si hubo un error
     */
    @PutMapping("/{matchId}")
    public ResponseEntity<GenericResponse> updateMatch(@PathVariable int matchId, @RequestBody CreateMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        boolean success = footballMatchService.updateMatch(matchId, request, userDetails);
        if (success) {
            return ResponseEntity.ok(new GenericResponse(true, "Match updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericResponse(false, "Failed to update match"));
        }
    }

    /**
     * Endpoint para actualizar el resultado de un partido de fútbol.
     *
     * @param matchId      el ID del partido a actualizar
     * @param result       el resultado del partido
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la actualización del resultado fue exitosa o si hubo un error
     */
    @PutMapping("/result/{matchId}")
    public ResponseEntity<String> updateMatchResult(@PathVariable int matchId, @RequestParam String result, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            boolean isUpdated = footballMatchService.updateMatchResult(matchId, result, user);
            if (isUpdated) {
                return ResponseEntity.ok("Match result updated successfully");
            } else {
                return ResponseEntity.status(403).body("Permission denied or match not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating match result: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener los jugadores del equipo local de un partido.
     *
     * @param matchId el ID del partido
     * @return una lista de jugadores del equipo local o un error si ocurre un problema
     */
    @GetMapping("/{matchId}/homeTeamPlayers")
    public ResponseEntity<List<User>> getHomeTeamPlayers(@PathVariable int matchId) {
        List<User> players = footballMatchService.getHomeTeamPlayers(matchId);
        if (players == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(players);
    }

    /**
     * Endpoint para obtener los jugadores del equipo visitante de un partido.
     *
     * @param matchId el ID del partido
     * @return una lista de jugadores del equipo visitante o un error si ocurre un problema
     */
    @GetMapping("/{matchId}/awayTeamPlayers")
    public ResponseEntity<List<User>> getAwayTeamPlayers(@PathVariable int matchId) {
        List<User> players = footballMatchService.getAwayTeamPlayers(matchId);
        if (players == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(players);
    }
}
