package ncolrod.socialfutv3.api.requests;

public class UpdateRoleRequest {
    private int userId;
    private String role;

    public UpdateRoleRequest(int userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    // Getters y setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
