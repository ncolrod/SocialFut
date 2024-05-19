package ncolrod.socialfut.services;

import ncolrod.socialfut.entities.FootballMatch;
import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.FootballMatchRepository;
import ncolrod.socialfut.requests.CreateMatchRequest;
import ncolrod.socialfut.requests.JoinMatchRequest;
import ncolrod.socialfut.responses.CreateMatchResponse;
import ncolrod.socialfut.responses.JoinMatchResponse;
import org.hibernate.Hibernate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class FootballMatchService {

    private final FootballMatchRepository footballMatchRepository;

    public FootballMatchService(FootballMatchRepository footballMatchRepository) {
        this.footballMatchRepository = footballMatchRepository;
    }

    public CreateMatchResponse createMatch(CreateMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        try {
            if (user != null && user.getRole() != Role.USER) {
                FootballMatch footballMatch = new FootballMatch(
                        request.getHomeTeam(),
                        request.getLocation(),
                        user,
                        request.getDate(),
                        request.getPricePerPerson()
                );
                FootballMatch footballMatch1 = footballMatchRepository.save(footballMatch);
                System.out.println(footballMatch1.getId());

                return new CreateMatchResponse(true, "Match created successfully", footballMatch.getId());
            } else {
                return new CreateMatchResponse(false, "Unauthorized or insufficient permissions");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateMatchResponse(false, "Error creating match");
        }
    }

    public JoinMatchResponse joinMatch(JoinMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Team awayTeam = user.getTeam();
        System.out.println(request.toString());
        try {
            if (user != null && user.getRole() != Role.USER) {
                FootballMatch footballMatch = footballMatchRepository.findById(request.getMatchId()).orElseThrow();
                Hibernate.initialize(footballMatch.getHomeTeam().getUsers()); // Initialize the collection
                Hibernate.initialize(footballMatch.getAwayTeam().getUsers());

                if (footballMatch != null) {
                    footballMatch.setAwayTeam(awayTeam);
                    footballMatchRepository.save(footballMatch);
                    return new JoinMatchResponse(true, "Successfully joined the match", footballMatch);
                } else {
                    return new JoinMatchResponse(false, "Match not found");
                }
            } else {
                return new JoinMatchResponse(false, "Unauthorized or insufficient permissions");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JoinMatchResponse(false, "Error joining match");
        }
    }
}
