package rs.ns.lu.feature.users;

import java.util.Base64;
import java.util.Optional;

import javax.transaction.Transactional;

import org.camunda.bpm.engine.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.api.User;
import rs.ns.lu.feature.tasks.TaskService;
import rs.ns.lu.util.ConstantsUtil;

@Slf4j
@Component
@Transactional
@AllArgsConstructor
class UserServiceImpl implements UserService {

	private final TaskService taskService;
	private final UserRepository userRepository;
	private final org.camunda.bpm.engine.TaskService camundaTaskService;

	@Override
	public User loginAndEnrich(final User loginUser) {
		final var existing = findByUsername(loginUser.getUsername());
		if (existing.isEmpty() || !existing.get().getPassword().equals(loginUser.getPassword()) || !existing.get().isVerified()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found or password not matched!");
		}
		return User.builder().id(existing.get().getId()).username(existing.get().getUsername())
			.name(existing.get().getFirstName() + " " + existing.get().getLastName())
			.role(existing.get().getRole()).build();
	}

	@Override
	public Optional<UserEntity> findByUsername(final String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public void save(final UserEntity user) {
		userRepository.save(user);
	}

	@Override
	public void verifyUser(final String verificationCode) {
		final var decodedVerificationCode = new String(Base64.getDecoder().decode(verificationCode));
		log.info("Received verification code: {}", decodedVerificationCode);
		final var verificationCodeComponents = decodedVerificationCode.split(ConstantsUtil.ACTIVATION_CODE_DELIMITER);
		final Task task = camundaTaskService.createTaskQuery()
			.processInstanceId(verificationCodeComponents[0])
			.active()
			.singleResult();
		final var username = verificationCodeComponents[1];
		final var existing = findByUsername(username);
		if (existing.isEmpty()) {
			throw new IllegalArgumentException(String.format("User %s not found and cannot be verified!", username));
		}
		taskService.completeTask(
			Optional.ofNullable(task).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User verification task not found!")).getId());
		userRepository.save(existing.get().toBuilder().verified(true).build());
	}

}
