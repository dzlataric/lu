package rs.ns.lu.feature.tasks;

import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class VotingOnCompleteListener implements TaskListener {

	private final FormService formService;

	@Override
	public void notify(final DelegateTask delegateTask) {
		final var results = (List<String>) delegateTask.getVariable("votingResults");

		formService.getTaskFormData(delegateTask.getId()).getFormFields().forEach(ff -> results.add((String) ff.getValue().getValue()));

		delegateTask.setVariable("votingResults", results);
	}
}
