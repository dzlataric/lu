package rs.ns.lu.feature.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class SendScriptService implements JavaDelegate {
	@Override
	public void execute(final DelegateExecution execution) throws Exception {
		//todo send email notification here
	}
}
