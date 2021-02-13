package rs.ns.lu.feature.tasks;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.api.Role;
import rs.ns.lu.feature.users.UserEntity;
import rs.ns.lu.feature.users.UserRepository;

@Slf4j
@Component
@AllArgsConstructor
public class SetBoardMembersService implements JavaDelegate {

	private final UserRepository userRepository;

	@Override
	public void execute(final DelegateExecution execution) {
		final var boardMembers = userRepository.findByRole(Role.BOARD).stream().map(UserEntity::getUsername).collect(Collectors.toList());

		log.info("Setting board members {}", String.join(", ", boardMembers));

		execution.setVariable("boardMembers", boardMembers);
		execution.setVariable("votingResults", new ArrayList<String>());
		execution.setVariable("cycle", 0);
	}
}
