package rs.ns.lu.feature.users;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.feature.email.EmailService;

@Slf4j
@Component
@AllArgsConstructor
public class SendUserRejectionService implements JavaDelegate {

	private final EmailService emailService;

	@Override
	public void execute(final DelegateExecution execution) {
		emailService.sendUserRejected((String) execution.getVariable("email"));
	}
}
