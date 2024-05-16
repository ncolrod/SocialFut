package ncolrod.socialfutv3.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @JsonCreator
    public RegisterRequest( @JsonProperty String firstname,@JsonProperty String lastname, @JsonProperty String telephone, @JsonProperty String location,@JsonProperty String position,@JsonProperty String email,@JsonProperty String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.location = location;
        this.position = position;
        this.email = email;
        this.password = password;
    }
}
