package ncolrod.socialfutv3.api.models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un equipo de fútbol con todos sus detalles.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Serializable {

    /** Identificador único del equipo. */
    private int id;

    /** Nombre del equipo. */
    private String name;

    /** Ubicación del equipo. */
    private String location;

    /** Estadio donde juega el equipo. */
    private String stadium;

    /** Código de unión del equipo. */
    private String join_code;

    /** Color del equipo. */
    private String team_color;

    /** Descripción del equipo. */
    private String description;

    /** Capitán del equipo. */
    private User captain;

    /** Lista de usuarios que pertenecen al equipo. */
    private List<User> users;

    /** Indica si el equipo está disponible para unirse a un partido. */
    private boolean isAvailable;

    // Estadísticas
    /** Número de partidos jugados por el equipo. */
    private int matchesPlayed;

    /** Número de partidos ganados por el equipo. */
    private int matchesWon;

    /** Número de partidos perdidos por el equipo. */
    private int lostMatches;

    /** Número de partidos empatados por el equipo. */
    private int tiedMatches;
}
