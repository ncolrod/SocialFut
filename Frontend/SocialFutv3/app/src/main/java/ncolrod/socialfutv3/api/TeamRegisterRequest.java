package ncolrod.socialfutv3.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
public class TeamRegisterRequest {

    @JsonProperty private String name;
    @JsonProperty private String location;
    @JsonProperty private String stadium;
    @JsonProperty private String join_code;
    @JsonProperty private String team_color;
    @JsonProperty private String description;

    //@ConstructorProperties({"name", "location", "stadium", "join_code", "team_color", "description"})
    public TeamRegisterRequest(String name, String location, String stadium, String join_code, String team_color, String description) {
        this.name = name;
        this.location = location;
        this.stadium = stadium;
        this.join_code = join_code;
        this.team_color = team_color;
        this.description = description;
    }
}
