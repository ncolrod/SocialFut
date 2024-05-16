package ncolrod.socialfutv3.api.retrofit;

public class TokenHolder {
    private static TokenHolder instance = new TokenHolder();

    private String token = null;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static TokenHolder getInstance() {
        return instance;
    }
}