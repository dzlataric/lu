package rs.ns.lu.feature.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.api.Role;
import rs.ns.lu.feature.email.EmailService;

@Slf4j
@Component
@AllArgsConstructor
public class CreateUserService implements JavaDelegate {

	private final EmailService emailService;
	private final UserService userService;
	private final GenreRepository genreRepository;
	private final IdentityService identityService;

	@Override
	public void execute(final DelegateExecution execution) {
		final var userEntity = buildUserFromProcessVariables(execution);
		if (userService.findByUsername(userEntity.getUsername()).isPresent()) {
			throw new IllegalArgumentException("User with username {} already exists!");
		}

		final var camundaUser = identityService.newUser(userEntity.getUsername());
		camundaUser.setPassword(userEntity.getPassword());
		camundaUser.setFirstName(userEntity.getFirstName());
		camundaUser.setLastName(userEntity.getLastName());
		camundaUser.setEmail(userEntity.getEmail());

		log.info("Storing new user: {}", userEntity.getUsername());

		userService.save(userEntity);
		identityService.saveUser(camundaUser);
		emailService.sendUserConfirmation(execution.getProcessInstanceId(), userEntity);
		if (Role.WRITER == userEntity.getRole()) {
			execution.setVariable("uploadedTexts", new ArrayList<String>());
		}
	}

	private UserEntity buildUserFromProcessVariables(final DelegateExecution execution) {
		final var builder = UserEntity.builder()
			.firstName(execution.getVariable("firstName").toString())
			.lastName(execution.getVariable("lastName").toString())
			.username(execution.getVariable("username").toString())
			.password(execution.getVariable("password").toString())
			.email(execution.getVariable("email").toString())
			.city(execution.getVariable("city").toString())
			.country(execution.getVariable("country").toString())
			.role(Role.valueOf(execution.getVariable("role").toString()))
			.betaReader(execution.getVariable("beta") != null && Boolean.parseBoolean(execution.getVariable("role").toString()))
			.genres(((List<String>) execution.getVariable("genres")).stream().map(id -> genreRepository.findById(Long.valueOf(id)))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toSet()))
			.enabled(false);

		final var betaGenres = execution.getVariable("betaGenres");
		if (betaGenres != null && betaGenres instanceof List) {
			builder.betaGenres(((List<String>) betaGenres).stream().map(id -> genreRepository.findById(Long.valueOf(id)))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toSet()));
		}

		return builder.build();
	}
}
