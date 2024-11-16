package glsib.parkingauth.services;

import glsib.parkingauth.dtos.CreateTableDto;
import glsib.parkingauth.dtos.LoginUserDto;
import glsib.parkingauth.dtos.RegisterUserDto;
import glsib.parkingauth.entities.Role;
import glsib.parkingauth.entities.User;
import glsib.parkingauth.entities.Zone;
import glsib.parkingauth.repositories.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import glsib.parkingauth.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ZoneRepository zoneRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            ZoneRepository zoneRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.zoneRepository = zoneRepository;
    }

    public User signup(RegisterUserDto input) {
        logger.info("Attempting to sign up user with email: {}", input.getEmail());

        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            logger.warn("Email already exists: {}", input.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        logger.info("Email is available for registration.");

        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        logger.info("Creating user: {}", user);

        User savedUser = userRepository.save(user);
        logger.info("User successfully registered: {}", savedUser);
        return savedUser;
    }
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Zone createZone(CreateTableDto input) {
        logger.info("Attempting to sign up user with longitude and latitude: {}", input.getLongitude(), input.getLatitude());

        if (zoneRepository.findByLatitude(input.getLatitude()).isPresent() && zoneRepository.findByLongitude(input.getLongitude()).isPresent()) {
            logger.warn("Parking already exists: {}", input.getLongitude(), input.getLatitude());
            throw new IllegalArgumentException("Parking already exists");
        }

        logger.info("Email is available for registration.");

        Date currentDate = new Date();
        Zone zone = new Zone();
        zone.setAvailableSpots(input.getAvailableSpots());
        zone.setCreatedAt(currentDate);
        zone.setUpdatedAt(currentDate);
        zone.setDescription(input.getDescription());
        zone.setLatitude(input.getLatitude());
        zone.setLongitude(input.getLongitude());
        zone.setPrice(input.getPrix());
        zone.setTotalSpots(input.getTotalSpots());

        logger.info("Creating Parking: {}", zone);

        Zone savedZone = zoneRepository.save(zone);
        logger.info("User successfully registered: {}", savedZone);
        return savedZone;
    }

}
