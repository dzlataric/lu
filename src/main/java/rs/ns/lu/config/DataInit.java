package rs.ns.lu.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import rs.ns.lu.feature.users.GenreEntity;
import rs.ns.lu.feature.users.GenreRepository;

@Component
@Transactional
@AllArgsConstructor
public class DataInit {

	private final GenreRepository genreRepository;

	@PostConstruct
	public void initGenres() {
		final var genre1 = GenreEntity.builder().name("Prvi").build();
		final var genre2 = GenreEntity.builder().name("Drugi").build();
		final var genre3 = GenreEntity.builder().name("Treci").build();
		genreRepository.saveAll(List.of(genre1, genre2, genre3));
	}

}
