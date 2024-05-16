package ncolrod.socialfut.controllers;

import ncolrod.socialfut.requests.CreateMatchRequest;
import ncolrod.socialfut.requests.JoinMatchRequest;
import ncolrod.socialfut.responses.CreateMatchResponse;
import ncolrod.socialfut.responses.JoinMatchResponse;
import ncolrod.socialfut.services.FootballMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class FootballMatchController {

    private final FootballMatchService footballMatchService;


    public FootballMatchController(FootballMatchService footballMatchService) {
        this.footballMatchService = footballMatchService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateMatchResponse> createMatch(@RequestBody CreateMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        CreateMatchResponse response = footballMatchService.createMatch(request, userDetails);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<JoinMatchResponse> joinMatch(@RequestBody JoinMatchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        JoinMatchResponse response = footballMatchService.joinMatch(request, userDetails);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}



