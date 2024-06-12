package ncolrod.socialfutv3.api.requests;

/**
 * Representa una solicitud para actualizar el rol de un usuario.
 */
public class UpdateRoleRequest {

    /**
     * El ID del usuario cuyo rol se actualizará.
     */
    private int userId;

    /**
     * El nuevo rol del usuario.
     */
    private String role;

    /**
     * Constructor que inicializa el ID del usuario y el nuevo rol.
     *
     * @param userId El ID del usuario.
     * @param role El nuevo rol del usuario.
     */
    public UpdateRoleRequest(int userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    /**
     * Obtiene el ID del usuario.
     *
     * @return El ID del usuario.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param userId El ID del usuario.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Obtiene el nuevo rol del usuario.
     *
     * @return El nuevo rol del usuario.
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece el nuevo rol del usuario.
     *
     * @param role El nuevo rol del usuario.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
