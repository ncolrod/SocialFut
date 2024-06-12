package ncolrod.socialfutv3.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

/**
 * Representa una solicitud para crear un partido de fútbol.
 */
@Data
@Builder
@NoArgsConstructor
public class CreateMatchRequest {

    /** Equipo local para el partido. */
    @JsonProperty
    private Team homeTeam;

    /** Ubicación del partido. */
    @JsonProperty
    private String location;

    /** Usuario creador del partido. */
    @JsonProperty
    private User creatorUser;

    /** Fecha y hora del partido. */
    @JsonProperty
    private Timestamp date;

    /** Precio por persona para el partido. */
    @JsonProperty
    private double pricePerPerson;

    /**
     * Constructor de la clase CreateMatchRequest.
     *
     * @param homeTeam Equipo local para el partido.
     * @param location Ubicación del partido.
     * @param creatorUser Usuario creador del partido.
     * @param date Fecha y hora del partido.
     * @param pricePerPerson Precio por persona para el partido.
     */
    @JsonCreator
    public CreateMatchRequest(
            @JsonProperty Team homeTeam,
            @JsonProperty String location,
            @JsonProperty User creatorUser,
            @JsonProperty Timestamp date,
            @JsonProperty Double pricePerPerson) {
        this.homeTeam = homeTeam;
        this.location = location;
        this.creatorUser = creatorUser;
        this.date = date;
        this.pricePerPerson = pricePerPerson;
    }
}
