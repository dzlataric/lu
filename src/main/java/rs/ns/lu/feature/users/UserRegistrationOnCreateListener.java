package rs.ns.lu.feature.users;

import java.util.stream.Collectors;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class UserRegistrationOnCreateListener implements TaskListener {

	private final FormService formService;
	private final GenreRepository genreRepository;

	@Override
	public void notify(final DelegateTask delegateTask) {
		log.info("Initializing user registration genres form field");
		final var genres = genreRepository.findAll().stream().collect(Collectors.toMap(g -> String.valueOf(g.getId()), GenreEntity::getName));
		formService.getTaskFormData(delegateTask.getId()).getFormFields().forEach(ff -> {
			if (ff.getType() instanceof EnumFormType) {
				final var values = ((EnumFormType) ff.getType()).getValues();
				genres.forEach(values::put);
			}
		});
	}
}
