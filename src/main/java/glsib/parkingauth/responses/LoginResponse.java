package glsib.parkingauth.responses;

public class LoginResponse {
    private String token;
    private long expiresIn;

    // Method for setting the token with method chaining
    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    // Method for setting the expiration time with method chaining
    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
