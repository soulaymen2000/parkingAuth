package glsib.parkingauth.controllers;

import glsib.parkingauth.dtos.CoordinatesDto;
import glsib.parkingauth.dtos.CreateTableDto;
import glsib.parkingauth.dtos.RegisterUserDto;
import glsib.parkingauth.entities.User;
import glsib.parkingauth.dtos.LoginUserDto;
import glsib.parkingauth.entities.Zone;
import glsib.parkingauth.services.AuthenticationService;
import glsib.parkingauth.services.JwtService;
import glsib.parkingauth.services.UserService;
import glsib.parkingauth.services.ZoneService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;// Added UserService
    private final PasswordEncoder passwordEncoder;
    private final ZoneService zoneService;

    @Autowired
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserService userService, PasswordEncoder passwordEncoder, ZoneService zoneService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService; // Inject UserService
        this.passwordEncoder = passwordEncoder;
        this.zoneService = zoneService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginUserDto", new LoginUserDto());
        return "login"; // Thymeleaf template name
    }

    @PostMapping("/login")
    public String authenticate(LoginUserDto loginUserDto, Model model, HttpServletResponse response) {
        try {
            // Authenticate user
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser, authenticatedUser.getRole());
            System.out.println("Generated JWT Token: " + jwtToken);
            System.out.println("Authenticated user: " + authenticatedUser.getEmail());

            // Create JWT Cookie
            addJwtCookie(response, jwtToken);

            // Determine role-based redirection
            String role = String.valueOf(authenticatedUser.getRole());
            System.out.println("Role: " + role); // Debug statement
            if ("ADMIN".equalsIgnoreCase(role)) {
                return "redirect:/admin"; // Redirect to admin dashboard
            } else {
                return "redirect:/home"; // Redirect to user home
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login"; // Return to login page with error message
        }
    }

    private void addJwtCookie(HttpServletResponse response, String jwtToken) {
        Cookie jwtCookie = new Cookie("JwtToken", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(86400); // 1 day
        response.addCookie(jwtCookie);

        // Log the cookie for debugging
        System.out.println("JWT Cookie Created: " + jwtCookie.getName() + " = " + jwtCookie.getValue());
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Clear the JWT cookie
        Cookie jwtCookie = new Cookie("JwtToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Set cookie max age to 0 to delete it
        response.addCookie(jwtCookie);

        return "redirect:/landing"; // Redirect to login page after logout
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("registerUserDto", new RegisterUserDto());
        return "signup"; // Return the name of the view (e.g., signup.html)
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("registerUserDto") RegisterUserDto registerUserDto, Model model) {
        try {
            User registeredUser = authenticationService.signup(registerUserDto);
            model.addAttribute("user", registeredUser);
            return "redirect:/auth/login"; // Return success view after registration
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup"; // Return the signup view with error message
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "signup"; // Return the signup view with error message
        }
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Model model, Principal principal) {
        String email = principal.getName();  // Get the logged-in user's email
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        return "edit-profile";  // This is the name of your Thymeleaf template
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes, Principal principal) {
        String email = principal.getName();  // Get the logged-in user's email
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the email is being updated
        if (user.getEmail() != null && !user.getEmail().equals(currentUser.getEmail())) {
            Optional<User> existingUser = userService.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Email is already in use.");
                return "redirect:/auth/profile/edit";
            }
            // Update email only if it's not already in use
            currentUser.setEmail(user.getEmail());
        }

        // Update other fields as needed
        currentUser.setFullName(user.getFullName()); // Update full name
        if (user.getRole() != null) {
            currentUser.setRole(user.getRole());  // Update role only if a new role is provided
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // Hash the new password before saving
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            currentUser.setPassword(encodedPassword);
        }

        // Save the updated user data
        userService.save(currentUser);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        return "redirect:/auth/profile/edit";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("createTableDto", new CreateTableDto());
        return "create-park";
    }

    @PostMapping("/create")
    public String createZone(@ModelAttribute("createTableDto") CreateTableDto createTableDto, Model model) {
        try {
            Zone createdZone = authenticationService.createZone(createTableDto);
            model.addAttribute("zone", createdZone);
            return "redirect:/admin"; // Return success view after registration
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "create-park"; // Return the signup view with error message
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "create-park"; // Return the signup view with error message
        }
    }

    @GetMapping("/update")
    public String showUpdateForm(Model model) {
        model.addAttribute("coordinatesDto", new CoordinatesDto());
        return "update-park"; // A form to enter latitude and longitude
    }


    @PostMapping("/update")
    public String processUpdateForm(@ModelAttribute("coordinatesDto") CoordinatesDto coordinatesDto, Model model) {
        double latitude = coordinatesDto.getLatitude();
        double longitude = coordinatesDto.getLongitude();

        // Check if the zone exists
        Optional<Zone> optionalZone = zoneService.findByLatitudeAndLongitude(latitude, longitude);

        if (optionalZone.isPresent()) {
            // Add the zone to the model and redirect to the update zone page
            model.addAttribute("zone", optionalZone.get());
            return "update-zone"; // Page for updating the zone
        } else {
            // Add error message and reload the form
            model.addAttribute("error", "Zone with specified coordinates not found.");
            return "update-park";
        }
    }

    @GetMapping("/update/zone")
    public String showZoneUpdateForm(@RequestParam(value = "latitude", required = false) Double latitude,
                                     @RequestParam(value = "longitude", required = false) Double longitude,
                                     Model model) {

        if (latitude != null && longitude != null) {
            try {
                Zone zone = zoneService.findByLatitudeAndLongitude(latitude, longitude)
                        .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

                model.addAttribute("zone", zone); // Add the zone object to the model for pre-filling the form
            } catch (Exception e) {
                model.addAttribute("error", "Zone not found. Please try again.");
            }
        }

        return "update-zone"; // Returns the Thymeleaf template for updating the zone
    }


    @PostMapping("/update/zone")
    public String updateZone(@ModelAttribute("coordinatesDto") CoordinatesDto coordinatesDto, Model model) {


        double latitude = coordinatesDto.getLatitude();
        double longitude = coordinatesDto.getLongitude();

        Date currentDate = new Date();
        try {
            Zone zone = zoneService.findByLatitudeAndLongitude(latitude, longitude)
                    .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

            // Update zone details
            zone.setTotalSpots(zone.getTotalSpots());
            zone.setAvailableSpots(zone.getAvailableSpots());
            zone.setUpdatedAt(currentDate);
            zoneService.saveZone(zone);

            return "redirect:/admin"; // Redirect to the admin page on success
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "update-zone"; // Stay on the update page in case of error
        }
    }



}
