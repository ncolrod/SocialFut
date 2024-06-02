package ncolrod.socialfutv3.api.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsUpdateRequest {
    private int playerId;
    private int goals;
    private int assists;
    private int participated;
}
