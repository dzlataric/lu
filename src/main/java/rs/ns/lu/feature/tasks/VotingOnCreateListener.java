package rs.ns.lu.feature.tasks;

import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.feature.users.UserService;
import rs.ns.lu.util.CollectionFormType;

@Slf4j
@Component
@AllArgsConstructor
public class VotingOnCreateListener implements TaskListener {

	private final UserService userService;
	private final FormService formService;

	@Override
	public void notify(final DelegateTask delegateTask) {
		log.info("Initializing text comments form field for voting form");
		final var uploadedTexts = (List<String>) delegateTask.getVariable("uploadedTexts");
		formService.getTaskFormData(delegateTask.getId()).getFormFields().forEach(ff -> {
			if (ff.getType() instanceof CollectionFormType) {
				final var values = ((EnumFormType) ff.getType()).getValues();
				uploadedTexts.forEach(ut -> values.put(ut, ""));
			}
		});
	}
}
