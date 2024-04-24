package ncolrod.socialfutv3.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String email;
    @JsonProperty
    private String password;

    @JsonCreator
    public RegisterRequest( @JsonProperty String firstname,@JsonProperty String lastname,@JsonProperty String email,@JsonProperty String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}
