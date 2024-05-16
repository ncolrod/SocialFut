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


    //@ConstructorProperties({"name", "location", "stadium", "join_code"})
    public TeamRegisterRequest(String name, String location, String stadium, String join_code) {
        this.name = name;
        this.location = location;
        this.stadium = stadium;
        this.join_code = join_code;

    }
}
