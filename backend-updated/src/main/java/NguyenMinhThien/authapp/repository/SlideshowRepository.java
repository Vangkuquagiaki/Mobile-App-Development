package NguyenMinhThien.authapp.repository;

import NguyenMinhThien.authapp.entity.Slideshow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideshowRepository extends JpaRepository<Slideshow, java.util.UUID> {
}
