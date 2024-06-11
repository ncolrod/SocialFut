package ncolrod.socialfut.services;

import jakarta.persistence.EntityNotFoundException;
import ncolrod.socialfut.entities.FootballMatch;
import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.FootballMatchRepository;
import ncolrod.socialfut.repositories.TeamRepository;
import ncolrod.socialfut.requests.CreateMatchRequest;
import ncolrod.socialfut.requests.JoinMatchRequest;
import ncolrod.socialfut.responses.CreateMatchResponse;
import ncolrod.socialfut.responses.JoinMatchResponse;
import org.hibernate.Hibernate;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones relacionadas con los partidos de fútbol.
 */
@Service
public class FootballMatchService {

    private final FootballMatchRepository footballMatchRepository;
    private final TeamRepository teamRepository;

    public FootballMatchService(FootballMatchRepository footballMatchRepository, TeamRepository teamRepository) {
        this.footballMatchRepository = footballMatchRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Crea un partido de fútbol.
     *
     * @param request      la solicitud de creación del partido
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la creación fue exitosa
     */
    @Transactional
    public CreateMatchResponse createMatch(CreateMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            if (user != null && user.getRole() != Role.USER) {
                Team homeTeam = request.getHomeTeam();

                if (!homeTeam.isAvailable()) {
                    FootballMatch footballMatch = new FootballMatch(
                            homeTeam,
                            request.getLocation(),
                            user,
                            request.getDate(),
                            request.getPricePerPerson()
                    );

                    // Cuando un equipo crea un partido deja de estar disponible para unirse a otros partidos
                    user.getTeam().setAvailable(true);
                    teamRepository.save(user.getTeam());
                    FootballMatch savedMatch = footballMatchRepository.save(footballMatch);
                    System.out.println(savedMatch.getId());
                    return new CreateMatchResponse(true, "Match created successfully", savedMatch.getId());
                } else {
                    return new CreateMatchResponse(false, "Error: el equipo no está disponible");
                }
            } else {
                return new CreateMatchResponse(false, "Unauthorized or insufficient permissions");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateMatchResponse(false, "Error creating match: " + e.getMessage());
        }
    }

    /**
     * Permite a un usuario unirse a un partido.
     *
     * @param request      la solicitud de unión al partido
     * @param userDetails  los detalles del usuario autenticado
     * @return una respuesta indicando si la unión fue exitosa
     */
    @Transactional
    public JoinMatchResponse joinMatch(JoinMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Team awayTeam = user.getTeam();
        System.out.println(request.toString());

        if (user == null || user.getRole() == Role.USER) {
            return new JoinMatchResponse(false, "Unauthorized or insufficient permissions");
        }

        try {
            FootballMatch footballMatch = footballMatchRepository.findById(request.getMatchId())
                    .orElseThrow(() -> new RuntimeException("Match with ID " + request.getMatchId() + " not found"));

            if (footballMatch.getHomeTeam() != null) {
                Hibernate.initialize(footballMatch.getHomeTeam().getUsers());
            }

            if (footballMatch.getAwayTeam() != null) {
                Hibernate.initialize(footballMatch.getAwayTeam().getUsers());
            }

            // Cuando un equipo se une a un partido deja de estar disponible para unirse a otros partidos
            footballMatch.setCreated(true);
            footballMatch.setAwayTeam(awayTeam);
            user.getTeam().setAvailable(true);
            teamRepository.save(user.getTeam());
            footballMatchRepository.save(footballMatch);
            return new JoinMatchResponse(true, "Successfully joined the match", footballMatch);
        } catch (Exception e) {
            e.printStackTrace();
            return new JoinMatchResponse(false, "Error joining match: " + e.getMessage());
        }
    }

    /**
     * Encuentra todos los partidos de fútbol.
     *
     * @return una lista de todos los partidos de fútbol
     * @throws Exception si ocurre un error al recuperar los partidos
     */
    public List<FootballMatch> findAllMatches() throws Exception {
        try {
            return footballMatchRepository.findAll();
        } catch (DataAccessException e) {
            throw new Exception("Error al acceder a los datos de los partidos", e);
        } catch (Exception e) {
            throw new Exception("Error desconocido al recuperar los partidos de fútbol", e);
        }
    }

    /**
     * Lista los partidos a los que un equipo puede unirse.
     *
     * @param userDetails los detalles del usuario autenticado
     * @return una lista de partidos a los que el equipo puede unirse
     * @throws Exception si ocurre un error al recuperar los partidos
     */
    public List<FootballMatch> listJoinMatches(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        int teamJoinId = user.getTeam().getId();
        try {
            return footballMatchRepository.listMatchToJoin(teamJoinId);
        } catch (DataAccessException e) {
            throw new Exception("Error al acceder a los datos de los partidos", e);
        } catch (Exception e) {
            throw new Exception("Error desconocido al recuperar los partidos de fútbol", e);
        }
    }

    /**
     * Lista los partidos jugados por un equipo.
     *
     * @param userDetails los detalles del usuario autenticado
     * @return una lista de partidos jugados por el equipo
     * @throws Exception si ocurre un error al recuperar los partidos
     */
    public List<FootballMatch> listMatchesPlayed(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        int teamJoinId = user.getTeam().getId();
        try {
            return footballMatchRepository.listMatchPlayed(teamJoinId);
        } catch (DataAccessException e) {
            throw new Exception("Error al acceder a los datos de los partidos", e);
        } catch (Exception e) {
            throw new Exception("Error desconocido al recuperar los partidos de fútbol", e);
        }
    }

    /**
     * Cancela un partido de fútbol.
     *
     * @param matchId      el ID del partido a cancelar
     * @param userDetails  los detalles del usuario autenticado
     * @return true si el partido fue cancelado, false en caso contrario
     */
    @Transactional
    public boolean cancelMatch(int matchId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        if (user == null || user.getRole() == Role.USER) {
            return false;
        }

        try {
            Optional<FootballMatch> matchOptional = footballMatchRepository.findById(matchId);
            if (matchOptional.isPresent()) {
                FootballMatch match = matchOptional.get();
                if (match.isCreated()) {
                    match.setCreated(false);
                    Team visitorTeam = match.getAwayTeam();
                    if (visitorTeam != null) {
                        visitorTeam.setAvailable(false);
                        teamRepository.save(visitorTeam);
                    }
                    match.setAwayTeam(null);
                    footballMatchRepository.save(match);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un partido de fútbol.
     *
     * @param matchId      el ID del partido a eliminar
     * @param userDetails  los detalles del usuario autenticado
     * @return true si el partido fue eliminado, false en caso contrario
     */
    @Transactional
    public boolean deleteMatch(int matchId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            Optional<FootballMatch> matchOptional = footballMatchRepository.findById(matchId);
            if (matchOptional.isPresent()) {
                FootballMatch match = matchOptional.get();
                if (match.getCreatorUser().getId() == user.getId()) {
                    Team awayTeam = match.getAwayTeam();
                    Team localTeam = match.getHomeTeam();
                    if (awayTeam != null) {
                        awayTeam.setAvailable(false);
                        teamRepository.save(awayTeam);
                    }
                    localTeam.setAvailable(false);
                    teamRepository.save(localTeam);
                    footballMatchRepository.delete(match);
                    return true;
                } else {
                    throw new AccessDeniedException("Unauthorized to delete this match");
                }
            } else {
                throw new EntityNotFoundException("Match not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza los detalles de un partido de fútbol.
     *
     * @param matchId      el ID del partido a actualizar
     * @param request      la solicitud de actualización del partido
     * @param userDetails  los detalles del usuario autenticado
     * @return true si el partido fue actualizado, false en caso contrario
     */
    public boolean updateMatch(int matchId, CreateMatchRequest request, UserDetails userDetails) {
        FootballMatch match = footballMatchRepository.findById(matchId).orElse(null);
        if (match == null) {
            return false;
        }

        if (!match.getCreatorUser().getUsername().equals(userDetails.getUsername())) {
            return false;
        }

        match.setDate(request.getDate());
        match.setLocation(request.getLocation());
        match.setPricePerPerson(request.getPricePerPerson());

        footballMatchRepository.save(match);
        return true;
    }

    /**
     * Actualiza el resultado de un partido de fútbol.
     *
     * @param matchId  el ID del partido a actualizar
     * @param result   el resultado del partido
     * @param user     el usuario que actualiza el resultado
     * @return true si el resultado fue actualizado, false en caso contrario
     */
    public boolean updateMatchResult(int matchId, String result, User user) {
        Optional<FootballMatch> matchOptional = footballMatchRepository.findById(matchId);
        if (matchOptional.isPresent()) {
            FootballMatch match = matchOptional.get();
            if (match.getCreatorUser().equals(user)) {
                match.setResult(result);
                match.setFinished(true);
                footballMatchRepository.save(match);
                updateTeamStatistics(match, result);
                return true;
            }
        }
        return false;
    }

    /**
     * Actualiza las estadísticas de los equipos después de un partido.
     *
     * @param match   el partido de fútbol
     * @param result  el resultado del partido
     */
    private void updateTeamStatistics(FootballMatch match, String result) {
        String[] scores = result.split("-");
        int homeTeamScore = Integer.parseInt(scores[0]);
        int awayTeamScore = Integer.parseInt(scores[1]);

        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        homeTeam.setMatchesPlayed(homeTeam.getMatchesPlayed() + 1);
        awayTeam.setMatchesPlayed(awayTeam.getMatchesPlayed() + 1);

        if (homeTeamScore > awayTeamScore) {
            homeTeam.setMatchesWon(homeTeam.getMatchesWon() + 1);
            awayTeam.setLostMatches(awayTeam.getLostMatches() + 1);
        } else if (homeTeamScore < awayTeamScore) {
            awayTeam.setMatchesWon(awayTeam.getMatchesWon() + 1);
            homeTeam.setLostMatches(homeTeam.getLostMatches() + 1);
        } else {
            homeTeam.setTiedMatches(homeTeam.getTiedMatches() + 1);
            awayTeam.setTiedMatches(awayTeam.getTiedMatches() + 1);
        }

        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);
    }

    /**
     * Obtiene los jugadores del equipo local de un partido.
     *
     * @param matchId el ID del partido
     * @return una lista de jugadores del equipo local
     */
    public List<User> getHomeTeamPlayers(int matchId) {
        FootballMatch match = footballMatchRepository.findById(matchId).orElse(null);
        if (match == null || match.getHomeTeam() == null) {
            return null;
        }
        return match.getHomeTeam().getUsers();
    }

    /**
     * Obtiene los jugadores del equipo visitante de un partido.
     *
     * @param matchId el ID del partido
     * @return una lista de jugadores del equipo visitante
     */
    public List<User> getAwayTeamPlayers(int matchId) {
        FootballMatch match = footballMatchRepository.findById(matchId).orElse(null);
        if (match == null || match.getAwayTeam() == null) {
            return null;
        }
        return match.getAwayTeam().getUsers();
    }
}
