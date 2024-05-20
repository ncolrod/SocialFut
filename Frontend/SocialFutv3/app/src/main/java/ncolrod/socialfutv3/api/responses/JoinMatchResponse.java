package ncolrod.socialfutv3.api.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ncolrod.socialfutv3.api.models.Match;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JoinMatchResponse {

    private boolean success;
    private String message;
    private Match footballMatch;

}
