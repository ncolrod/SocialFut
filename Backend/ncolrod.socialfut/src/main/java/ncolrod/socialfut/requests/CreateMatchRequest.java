package ncolrod.socialfut.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ncolrod.socialfut.entities.Team;
import ncolrod.socialfut.entities.User;

import java.sql.Timestamp;

/**
 * Clase que representa una solicitud para crear un partido de fútbol.
 */
@Data
@Builder
@NoArgsConstructor
public class CreateMatchRequest {

    /**
     * El equipo local que jugará el partido.
     */
    @JsonProperty
    private Team homeTeam;

    /**
     * La ubicación donde se jugará el partido.
     */
    @JsonProperty
    private String location;

    /**
     * El usuario que crea el partido.
     */
    @JsonProperty
    private User creatorUser;

    /**
     * La fecha y hora del partido.
     */
    @JsonProperty
    private Timestamp date;

    /**
     * El precio por persona para participar en el partido.
     */
    @JsonProperty
    private double pricePerPerson;

    /**
     * Constructor que inicializa todos los campos de la solicitud.
     *
     * @param homeTeam       el equipo local que jugará el partido
     * @param location       la ubicación donde se jugará el partido
     * @param creatorUser    el usuario que crea el partido
     * @param date           la fecha y hora del partido
     * @param pricePerPerson el precio por persona para participar en el partido
     */
    public CreateMatchRequest(Team homeTeam, String location, User creatorUser, Timestamp date, double pricePerPerson) {
        this.homeTeam = homeTeam;
        this.location = location;
        this.creatorUser = creatorUser;
        this.date = date;
        this.pricePerPerson = pricePerPerson;
    }
}
