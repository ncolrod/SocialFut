package ncolrod.socialfut.services;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.UniqueConstraint;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FootballMatchService {

    private final FootballMatchRepository footballMatchRepository;
    private final TeamRepository teamRepository;

    public FootballMatchService(FootballMatchRepository footballMatchRepository, TeamRepository teamRepository) {
        this.footballMatchRepository = footballMatchRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public CreateMatchResponse createMatch(CreateMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            if (user != null && user.getRole() != Role.USER) {
                Team homeTeam = request.getHomeTeam();

                if (!homeTeam.isAvailable()){
                    FootballMatch footballMatch = new FootballMatch(
                            homeTeam,
                            request.getLocation(),
                            user,
                            request.getDate(),
                            request.getPricePerPerson()
                    );

                    /*
                    Cuando un equipo crea un partido deja de estar disponible para unirse a otros partidos
                    */

                    user.getTeam().setAvailable(true);
                    teamRepository.save(user.getTeam());
                    FootballMatch savedMatch = footballMatchRepository.save(footballMatch);
                    System.out.println(savedMatch.getId());
                    return new CreateMatchResponse(true, "Match created successfully", savedMatch.getId());

                } else {
                    return new CreateMatchResponse(false, "Error: el equipo no esta disponible");
                }

            } else {
                return new CreateMatchResponse(false, "Unauthorized or insufficient permissions");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new CreateMatchResponse(false, "Error creating match: " + e.getMessage());
        }
    }

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

            /*
            Cuando un equipo se une a un partido deja de estar disponible para unirse a otros partidos
             */
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

    public List<FootballMatch> findAllMatches() throws Exception {
        try {
            // Intenta recuperar todos los partidos de fútbol
            return footballMatchRepository.findAll();
        } catch (DataAccessException e) {
            // Si hay un problema al acceder a los datos, lanza una excepción
            throw new Exception("Error al acceder a los datos de los partidos", e);
        } catch (Exception e) {
            // Captura cualquier otra excepción no esperada y lanza una excepción general
            throw new Exception("Error desconocido al recuperar los partidos de fútbol", e);
        }
    }

    public List<FootballMatch> listJoinMatches(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        int teamJoinId = user.getTeam().getId();
        try {
            // Intenta recuperar todos los partidos de fútbol menos los suyos propios
            return footballMatchRepository.listMatchToJoin(teamJoinId);
        } catch (DataAccessException e) {
            // Si hay un problema al acceder a los datos, lanza una excepción
            throw new Exception("Error al acceder a los datos de los partidos", e);
        } catch (Exception e) {
            // Captura cualquier otra excepción no esperada y lanza una excepción general
            throw new Exception("Error desconocido al recuperar los partidos de fútbol", e);
        }
    }

    public List<FootballMatch> listMatchesPlayed(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        int teamJoinId = user.getTeam().getId();
        try {
            // Recuperamos los partidos para mostrar los que hemos jugado
            return footballMatchRepository.listMatchPlayed(teamJoinId);
        } catch (DataAccessException e) {
            // Si hay un problema al acceder a los datos, lanza una excepción
            throw new Exception("Error al acceder a los datos de los partidos", e);
        } catch (Exception e) {
            // Captura cualquier otra excepción no esperada y lanza una excepción general
            throw new Exception("Error desconocido al recuperar los partidos de fútbol", e);
        }
    }

    @Transactional
    public boolean cancelMatch(int matchId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        if (user == null || user.getRole() == Role.USER) {
            return false; // O lanzar una excepción adecuada
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
                    if (awayTeam != null){
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

    public boolean updateMatch(int matchId, CreateMatchRequest request, UserDetails userDetails) {
        // Buscar el partido por ID
        FootballMatch match = footballMatchRepository.findById(matchId).orElse(null);
        if (match == null) {
            return false;
        }

        // Verificar si el usuario tiene permisos para actualizar el partido
        if (!match.getCreatorUser().getUsername().equals(userDetails.getUsername())) {
            return false;
        }

        // Actualizar los campos del partido
        match.setDate(request.getDate());
        match.setLocation(request.getLocation());
        match.setPricePerPerson(request.getPricePerPerson());
        // Actualizar otros campos según sea necesario

        // Guardar los cambios
        footballMatchRepository.save(match);
        return true;
    }

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
    private void updateTeamStatistics(FootballMatch match, String result) {
        String[] scores = result.split("-");
        int homeTeamScore = Integer.parseInt(scores[0]);
        int awayTeamScore = Integer.parseInt(scores[1]);

        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        // Increment matches played for both teams
        homeTeam.setMatchesPlayed(homeTeam.getMatchesPlayed() + 1);
        awayTeam.setMatchesPlayed(awayTeam.getMatchesPlayed() + 1);

        if (homeTeamScore > awayTeamScore) {
            // Home team wins
            homeTeam.setMatchesWon(homeTeam.getMatchesWon() + 1);
            awayTeam.setLostMatches(awayTeam.getLostMatches() + 1);
        } else if (homeTeamScore < awayTeamScore) {
            // Away team wins
            awayTeam.setMatchesWon(awayTeam.getMatchesWon() + 1);
            homeTeam.setLostMatches(homeTeam.getLostMatches() + 1);
        } else {
            // Tie
            homeTeam.setTiedMatches(homeTeam.getTiedMatches() + 1);
            awayTeam.setTiedMatches(awayTeam.getTiedMatches() + 1);
        }

        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);
    }

    public List<User> getHomeTeamPlayers(int matchId) {
        FootballMatch match = footballMatchRepository.findById(matchId).orElse(null);
        if (match == null || match.getHomeTeam() == null) {
            return null;
        }
        return match.getHomeTeam().getUsers();
    }

    public List<User> getAwayTeamPlayers(int matchId) {
        FootballMatch match = footballMatchRepository.findById(matchId).orElse(null);
        if (match == null || match.getAwayTeam() == null) {
            return null;
        }
        return match.getAwayTeam().getUsers();
    }


}
