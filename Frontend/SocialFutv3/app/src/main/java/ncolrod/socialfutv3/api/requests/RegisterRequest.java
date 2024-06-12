package ncolrod.socialfutv3.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud de registro de un nuevo usuario.
 */
@Data
@Builder
@NoArgsConstructor
public class RegisterRequest {

    /** El nombre del usuario. */
    @JsonProperty
    private String firstname;

    /** El apellido del usuario. */
    @JsonProperty
    private String lastname;

    /** El teléfono del usuario. */
    @JsonProperty
    private String telephone;

    /** La ubicación del usuario. */
    @JsonProperty
    private String location;

    /** La posición del usuario. */
    @JsonProperty
    private String position;

    /** El correo electrónico del usuario. */
    @JsonProperty
    private String email;

    /** La contraseña del usuario. */
    @JsonProperty
    private String password;

    /**
     * Constructor que inicializa todos los atributos de la solicitud de registro.
     *
     * @param firstname El nombre del usuario.
     * @param lastname El apellido del usuario.
     * @param telephone El teléfono del usuario.
     * @param location La ubicación del usuario.
     * @param position La posición del usuario.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     */
    @JsonCreator
    public RegisterRequest(@JsonProperty String firstname, @JsonProperty String lastname, @JsonProperty String telephone,
                           @JsonProperty String location, @JsonProperty String position, @JsonProperty String email,
                           @JsonProperty String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.location = location;
        this.position = position;
        this.email = email;
        this.password = password;
    }
}
