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

                // Ensure the home team is persisted before creating the match
                //if (homeTeam.equals(null)) {
                //    teamRepository.save(homeTeam);
                //}

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

    public void updateCheckHome(int matchId, String result) {
        Optional<FootballMatch> optionalMatch = footballMatchRepository.findById(matchId);

        if (optionalMatch.isPresent()) {
            FootballMatch match = optionalMatch.get();
            match.setCheckHome(result);
            footballMatchRepository.save(match);
            verifyResults(match);  // Verify after updating checkHome
        } else {
            throw new EntityNotFoundException("Match not found");
        }
    }

    public void updateCheckAway(int matchId, String result) {
        Optional<FootballMatch> optionalMatch = footballMatchRepository.findById(matchId);

        if (optionalMatch.isPresent()) {
            FootballMatch match = optionalMatch.get();
            match.setCheckAway(result);
            footballMatchRepository.save(match);
            verifyResults(match);  // Verify after updating checkAway
        } else {
            throw new EntityNotFoundException("Match not found");
        }
    }

    public void verifyResults(FootballMatch match) {
        if (match.getCheckHome() != null && match.getCheckAway() != null) {
            if (match.getCheckHome().equals(match.getCheckAway())) {
                match.setResult(match.getCheckHome());
                match.setFinished(true);
                footballMatchRepository.save(match);
            } else {
                // Manejar el error cuando los resultados no coinciden
                throw new IllegalArgumentException("Los resultados no coinciden");
            }
        }
    }

    @Transactional
    public boolean deleteMatch(int matchId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            Optional<FootballMatch> matchOptional = footballMatchRepository.findById(matchId);
            if (matchOptional.isPresent()) {
                FootballMatch match = matchOptional.get();
                if (match.getCreatorUser().getId() == user.getId()) {
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

    @Transactional
    public boolean updateMatch(int matchId, CreateMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            Optional<FootballMatch> matchOptional = footballMatchRepository.findById(matchId);
            if (matchOptional.isPresent()) {
                FootballMatch match = matchOptional.get();
                if (match.getCreatorUser().getId() == user.getId()) {
                    match.setHomeTeam(request.getHomeTeam());
                    match.setLocation(request.getLocation());
                    match.setDate(request.getDate());
                    match.setPricePerPerson(request.getPricePerPerson());
                    footballMatchRepository.save(match);
                    return true;
                } else {
                    throw new SecurityException("Unauthorized to update this match");
                }
            } else {
                throw new EntityNotFoundException("Match not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
