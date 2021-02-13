package rs.ns.lu.feature.tasks;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class DetermineVotingResultsService implements JavaDelegate {

	@Override
	public void execute(final DelegateExecution execution) {
		final var results = (List<String>) execution.getVariable("votingResults");
		execution.setVariable("isDeclined", results.stream().filter(r -> r.equals("declined")).count() >= results.size() / 2);
		execution.setVariable("isReturned", results.stream().anyMatch(r -> r.equals("uploadMoreTexts")));
		execution.setVariable("isAccepted", results.stream().filter(r -> r.equals("accepted")).count() == results.size());
		final var currentCycle = (int) execution.getVariable("cycle");
		execution.setVariable("cycle", currentCycle + 1);
		if (execution.hasVariable("voting")) {
			execution.removeVariable("voting");
		}
		if (execution.hasVariable("votingResults")) {
			execution.removeVariable("votingResults");
		}
		if (execution.hasVariable("boardMembers")) {
			execution.removeVariable("boardMembers");
		}
		if (execution.hasVariable("addMore")) {
			execution.removeVariable("addMore");
		}
	}
}
