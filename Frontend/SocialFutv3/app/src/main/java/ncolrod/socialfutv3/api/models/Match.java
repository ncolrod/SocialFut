package ncolrod.socialfutv3.api.models;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un partido de fútbol con todos sus detalles.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match implements Serializable {

    /** Identificador único del partido. */
    private int id;

    /** Equipo local que participa en el partido. */
    private Team homeTeam;

    /** Equipo visitante que participa en el partido. */
    private Team awayTeam;

    /** Fecha y hora del partido. */
    private Timestamp date;

    /** Ubicación donde se jugará el partido. */
    private String location;

    /** Resultado del partido. */
    private String result;

    /** Resumen del partido. */
    private String summary;

    /** Precio por persona para asistir al partido. */
    private Double pricePerPerson;

    /** Usuario que creó el partido. */
    private User creatorUser;

    /** Indica si el partido ha sido creado. */
    private boolean isCreated;

    /** Indica si el partido ha terminado. */
    private boolean isFinished;

    /** Chequeo del equipo local (usado para verificaciones o estado). */
    private String checkHome;

    /** Chequeo del equipo visitante (usado para verificaciones o estado). */
    private String checkAway;
}
