package rs.ns.lu.feature.users;

import java.util.Base64;
import java.util.Optional;

import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import rs.ns.lu.feature.tasks.TaskService;

@Component
@AllArgsConstructor
class UserServiceImpl implements UserService {

	private final TaskService taskService;
	private final org.camunda.bpm.engine.TaskService camundaTaskService;

	@Override
	public void verifyUser(final String verificationCode) {
		final Task task = camundaTaskService.createTaskQuery()
			.processInstanceId(new String(Base64.getDecoder().decode(verificationCode)))
			.active()
			.singleResult();
		taskService.completeTask(Optional.ofNullable(task).orElseThrow(() -> new IllegalArgumentException("User verification task not found!")).getId());
	}

}
