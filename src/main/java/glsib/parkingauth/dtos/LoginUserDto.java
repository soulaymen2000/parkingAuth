package glsib.parkingauth.dtos;

import glsib.parkingauth.entities.Role;

public class LoginUserDto {
    private String email;

    private String password;

    public String getPassword() {
        return password;
    }
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
