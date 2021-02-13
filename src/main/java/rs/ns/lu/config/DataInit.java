package rs.ns.lu.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import rs.ns.lu.api.Role;
import rs.ns.lu.feature.users.GenreEntity;
import rs.ns.lu.feature.users.GenreRepository;
import rs.ns.lu.feature.users.UserEntity;
import rs.ns.lu.feature.users.UserRepository;

@Component
@Transactional
@AllArgsConstructor
public class DataInit {

	private final UserRepository userRepository;
	private final GenreRepository genreRepository;

	@PostConstruct
	public void init() {
		final var admin = UserEntity.builder().firstName("Admin").lastName("Admin").username("admin").password("admin").email("admin@admin.com").city("NS")
			.country("RS").verified(true).enabled(true).role(Role.ADMIN).build();
		final var boardMember1 = UserEntity.builder().firstName("Petar").lastName("Petrovic").username("petarboard").password("petar").email("petar@board.com")
			.city("NS").country("RS").verified(true).enabled(true).role(Role.BOARD).build();
		final var boardMember2 = UserEntity.builder().firstName("Marko").lastName("Markovic").username("markoboard").password("marko").email("marko@board.com")
			.city("NS").country("RS").verified(true).enabled(true).role(Role.BOARD).build();
		final var boardMember3 = UserEntity.builder().firstName("Jovan").lastName("Jovanovic").username("jovanboard").password("jovan").email("jovan@board.com")
			.city("NS").country("RS").verified(true).enabled(true).role(Role.BOARD).build();

		List.of(admin, boardMember1, boardMember2, boardMember3).forEach(u -> {
			if (userRepository.findByUsername(u.getUsername()).isEmpty()) {
				userRepository.save(u);
			}
		});

		List.of(GenreEntity.builder().name("Prvi").build(), GenreEntity.builder().name("Drugi").build(), GenreEntity.builder().name("Treci").build())
			.forEach(g -> {
				if (genreRepository.findByName(g.getName()).isEmpty()) {
					genreRepository.save(g);
				}
			});
	}

}
