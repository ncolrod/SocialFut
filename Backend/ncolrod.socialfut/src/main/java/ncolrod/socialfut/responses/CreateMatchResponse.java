package ncolrod.socialfut.responses;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchResponse {
    private boolean success;
    private String message;
    private int matchId;

    public CreateMatchResponse(boolean success) {
        this.success = success;
    }

    public CreateMatchResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
