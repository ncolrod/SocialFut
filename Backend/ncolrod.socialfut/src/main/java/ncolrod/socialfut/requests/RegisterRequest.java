package ncolrod.socialfut.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;

@Data
@Builder
@NoArgsConstructor
public class RegisterRequest {

    @JsonProperty
    private String firstname;
    @JsonProperty
    private String lastname;

    @JsonProperty
    private String telephone;

    @JsonProperty
    private String location;

    @JsonProperty
    private String position;
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;

    @JsonProperty
    private int team_id;

    @ConstructorProperties({"firstname", "lastname", "telephone", "location", "position", "email", "password", "team_id"})
    public RegisterRequest(String firstname, String lastname, String telephone, String location, String position, String email, String password, int team_id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.location = location;
        this.position = position;
        this.email = email;
        this.password = password;
        this.team_id = team_id;
    }
}
