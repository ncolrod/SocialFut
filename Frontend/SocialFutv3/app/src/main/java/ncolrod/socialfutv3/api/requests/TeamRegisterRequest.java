package ncolrod.socialfutv3.api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para registrar un equipo.
 */
@Data
@Builder
@NoArgsConstructor
public class TeamRegisterRequest {

    /**
     * El nombre del equipo.
     */
    @JsonProperty
    private String name;

    /**
     * La ubicación del equipo.
     */
    @JsonProperty
    private String location;

    /**
     * El estadio del equipo.
     */
    @JsonProperty
    private String stadium;

    /**
     * El código de unión del equipo.
     */
    @JsonProperty
    private String join_code;

    /**
     * Constructor que inicializa todos los atributos de la solicitud de registro de equipo.
     *
     * @param name El nombre del equipo.
     * @param location La ubicación del equipo.
     * @param stadium El estadio del equipo.
     * @param join_code El código de unión del equipo.
     */
    public TeamRegisterRequest(String name, String location, String stadium, String join_code) {
        this.name = name;
        this.location = location;
        this.stadium = stadium;
        this.join_code = join_code;
    }
}
