package glsib.parkingauth.dtos;

import glsib.parkingauth.entities.Role;

public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
    private String company; // Add company field
    private Role role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompany() {
        return company; // Getter for company
    }

    public void setCompany(String company) {
        this.company = company; // Setter for company
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
