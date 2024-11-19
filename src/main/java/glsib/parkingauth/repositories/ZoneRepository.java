package glsib.parkingauth.repositories;
import glsib.parkingauth.entities.User;
import glsib.parkingauth.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Optional<Zone> findByLatitude(double latitude);

    Optional<Zone> findByLongitude(double longitude);

    Optional<Zone> findByLatitudeAndLongitude(double latitude, double longitude);


    Zone findByDescription(String description);
}
