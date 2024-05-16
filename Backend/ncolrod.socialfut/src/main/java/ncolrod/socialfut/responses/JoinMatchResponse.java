package ncolrod.socialfut.responses;

import lombok.*;
import ncolrod.socialfut.entities.FootballMatch;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinMatchResponse {
    private boolean success;
    private String message;
    private FootballMatch footballMatch;

    public JoinMatchResponse(boolean success, FootballMatch footballMatch) {
        this.success = success;
        this.footballMatch = footballMatch;
    }

    public JoinMatchResponse(boolean success) {
        this.success = success;
    }

    public JoinMatchResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
