package glsib.parkingauth.repositories;
import glsib.parkingauth.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Zone findByDescription(String description);
}
