package rs.ns.lu.feature.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

	Optional<GenreEntity> findByName(String name);

}
