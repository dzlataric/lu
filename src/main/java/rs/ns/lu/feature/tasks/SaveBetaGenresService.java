package rs.ns.lu.feature.tasks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.feature.users.GenreEntity;
import rs.ns.lu.feature.users.GenreRepository;
import rs.ns.lu.feature.users.UserRepository;

@Slf4j
@Component
@Transactional
@AllArgsConstructor
public class SaveBetaGenresService implements JavaDelegate {

	private final UserRepository userRepository;
	private final GenreRepository genreRepository;

	@Override
	public void execute(final DelegateExecution execution) {
		final var username = execution.getVariable("username").toString();
		final var user = userRepository.findByUsername(username).orElseThrow();
		final var betaGenres = ((List<String>) execution.getVariable("betaGenres")).stream()
			.map(id -> genreRepository.findById(Long.valueOf(id)))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toSet());

		log.info("Saving beta genres {} for user {}", betaGenres.stream().map(GenreEntity::getName).collect(Collectors.joining(", ")), username);

		user.setBetaGenres(betaGenres);
		userRepository.save(user);
	}
}
