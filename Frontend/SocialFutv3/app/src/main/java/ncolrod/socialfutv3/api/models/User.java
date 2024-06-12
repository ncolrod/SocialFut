package ncolrod.socialfutv3.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Representa un usuario del sistema con todos sus detalles.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    /** Identificador único del usuario. */
    private int id;

    /** Nombre del usuario. */
    private String firstname;

    /** Apellido del usuario. */
    private String lastname;

    /** Teléfono del usuario. */
    private String telephone;

    /** Ubicación del usuario. */
    private String location;

    /** Posición del usuario en el equipo. */
    private String position;

    /** Correo electrónico del usuario. */
    private String email;

    /** Rol del usuario en el sistema. */
    private Role role;

    // Estadísticas
    /** Número de partidos jugados por el usuario. */
    private int matchesPlayed;

    /** Número de goles anotados por el usuario. */
    private int goals;

    /** Número de asistencias realizadas por el usuario. */
    private int assists;

    /** Indica si el usuario ha participado en un partido. */
    private boolean participated;

    /**
     * Constructor completo de la clase User.
     *
     * @param id Identificador único del usuario.
     * @param firstname Nombre del usuario.
     * @param lastname Apellido del usuario.
     * @param telephone Teléfono del usuario.
     * @param location Ubicación del usuario.
     * @param position Posición del usuario en el equipo.
     * @param email Correo electrónico del usuario.
     * @param role Rol del usuario en el sistema.
     */
    public User(int id, String firstname, String lastname, String telephone, String location, String position, String email, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.location = location;
        this.position = position;
        this.email = email;
        this.role = role;
    }

    /**
     * Constructor de la clase User sin el parámetro role.
     *
     * @param id Identificador único del usuario.
     * @param firstname Nombre del usuario.
     * @param lastname Apellido del usuario.
     * @param telephone Teléfono del usuario.
     * @param location Ubicación del usuario.
     * @param position Posición del usuario en el equipo.
     * @param email Correo electrónico del usuario.
     */
    public User(int id, String firstname, String lastname, String telephone, String location, String position, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.location = location;
        this.position = position;
        this.email = email;
    }

    /** Constructor vacío de la clase User. */
    public User() {
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return Rol del usuario.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param role Rol del usuario.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Obtiene el número de partidos jugados por el usuario.
     *
     * @return Número de partidos jugados.
     */
    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    /**
     * Establece el número de partidos jugados por el usuario.
     *
     * @param matchesPlayed Número de partidos jugados.
     */
    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    /**
     * Obtiene el número de goles anotados por el usuario.
     *
     * @return Número de goles anotados.
     */
    public int getGoals() {
        return goals;
    }

    /**
     * Establece el número de goles anotados por el usuario.
     *
     * @param goals Número de goles anotados.
     */
    public void setGoals(int goals) {
        this.goals = goals;
    }

    /**
     * Obtiene el número de asistencias realizadas por el usuario.
     *
     * @return Número de asistencias realizadas.
     */
    public int getAssists() {
        return assists;
    }

    /**
     * Establece el número de asistencias realizadas por el usuario.
     *
     * @param assists Número de asistencias realizadas.
     */
    public void setAssists(int assists) {
        this.assists = assists;
    }

    /**
     * Obtiene la posición del usuario en el equipo.
     *
     * @return Posición del usuario.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Establece la posición del usuario en el equipo.
     *
     * @param position Posición del usuario.
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return Identificador único del usuario.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id Identificador único del usuario.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return Nombre del usuario.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param firstname Nombre del usuario.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return Apellido del usuario.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param lastname Apellido del usuario.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return Correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email Correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el teléfono del usuario.
     *
     * @return Teléfono del usuario.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Establece el teléfono del usuario.
     *
     * @param telephone Teléfono del usuario.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Obtiene la ubicación del usuario.
     *
     * @return Ubicación del usuario.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Establece la ubicación del usuario.
     *
     * @param location Ubicación del usuario.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Indica si el usuario ha participado en un partido.
     *
     * @return true si ha participado, false en caso contrario.
     */
    public boolean isParticipated() {
        return participated;
    }

    /**
     * Establece si el usuario ha participado en un partido.
     *
     * @param participated true si ha participado, false en caso contrario.
     */
    public void setParticipated(boolean participated) {
        this.participated = participated;
    }
}
