package glsib.parkingauth.repositories;
import glsib.parkingauth.entities.User;
import glsib.parkingauth.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
<<<<<<< HEAD
    Optional<Zone> findByLatitude(double latitude);

    Optional<Zone> findByLongitude(double longitude);

    Optional<Zone> findByLatitudeAndLongitude(double latitude, double longitude);

=======
>>>>>>> ddbcf9eaaa750fda9b08e97e79a3d0601a02444d
    Zone findByDescription(String description);
}
