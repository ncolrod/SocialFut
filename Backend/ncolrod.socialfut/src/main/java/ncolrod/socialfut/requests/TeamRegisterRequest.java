package ncolrod.socialfut.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;

/**
 * Clase que representa una solicitud para registrar un nuevo equipo.
 */
@Data
@Builder
@NoArgsConstructor
public class TeamRegisterRequest {

    /**
     * Nombre del equipo.
     */
    @JsonProperty
    private String name;

    /**
     * Ubicación del equipo.
     */
    @JsonProperty
    private String location;

    /**
     * Estadio del equipo.
     */
    @JsonProperty
    private String stadium;

    /**
     * Código de unión para el equipo.
     */
    @JsonProperty
    private String join_code;

    /**
     * Constructor para la clase TeamRegisterRequest.
     *
     * @param name      el nombre del equipo
     * @param location  la ubicación del equipo
     * @param stadium   el estadio del equipo
     * @param join_code el código de unión para el equipo
     */
    @ConstructorProperties({"name", "location", "stadium", "join_code"})
    public TeamRegisterRequest(String name, String location, String stadium, String join_code) {
        this.name = name;
        this.location = location;
        this.stadium = stadium;
        this.join_code = join_code;
    }
}
