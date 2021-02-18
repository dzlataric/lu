package rs.ns.lu.feature.tasks;

import javax.transaction.Transactional;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.feature.users.UserRepository;

@Slf4j
@Component
@Transactional
@AllArgsConstructor
public class FinishRegistrationListener implements TaskListener {

	private final UserRepository userRepository;

	@Override
	public void notify(final DelegateTask delegateTask) {
		final var username = delegateTask.getVariable("username").toString();
		final var user = userRepository.findByUsername(username).orElseThrow();
		user.setEnabled(true);
		userRepository.save(user);
	}
}
