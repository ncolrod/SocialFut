package ncolrod.socialfut.services;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                if (homeTeam.equals(null)) {
                    teamRepository.save(homeTeam);
                }

                FootballMatch footballMatch = new FootballMatch(
                        homeTeam,
                        request.getLocation(),
                        user,
                        request.getDate(),
                        request.getPricePerPerson()
                );

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
            } else {
                footballMatch.setAwayTeam(awayTeam);
            }

            footballMatchRepository.save(footballMatch);
            return new JoinMatchResponse(true, "Successfully joined the match", footballMatch);
        } catch (Exception e) {
            e.printStackTrace();
            return new JoinMatchResponse(false, "Error joining match: " + e.getMessage());
        }
    }
}
