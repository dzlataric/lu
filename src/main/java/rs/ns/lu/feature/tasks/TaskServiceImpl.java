package rs.ns.lu.feature.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.api.FormField;
import rs.ns.lu.api.FormSubmission;
import rs.ns.lu.api.Role;
import rs.ns.lu.api.Task;
import rs.ns.lu.util.ConstantsUtil;

@Slf4j
@Component
@AllArgsConstructor
class TaskServiceImpl implements TaskService {

	private final RuntimeService runtimeService;
	private final org.camunda.bpm.engine.TaskService taskService;
	private final FormService formService;
	private final IdentityService identityService;

	@Override
	public Task startProcessAndGetFormFields(final Role role, final String username) {
		identityService.setAuthenticatedUserId(username);
		final var processInstance = runtimeService
			.startProcessInstanceByKey(Role.WRITER == role ? ConstantsUtil.WRITER_REGISTRATION_PROCESS_KEY : ConstantsUtil.READER_REGISTRATION_PROCESS_KEY);
		runtimeService.setVariable(processInstance.getId(), "role", role.name());
		if (Role.WRITER == role) {
			runtimeService.setVariable(processInstance.getId(), "uploaded", 0);
		}
		final var task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().stream().findFirst().orElseThrow();
		final var taskFormData = formService.getTaskFormData(task.getId());
		return Task.builder()
			.id(task.getId())
			.name(task.getName())
			.process(processInstance.getProcessDefinitionId())
			.processInstanceId(processInstance.getId())
			.formFields(taskFormData.getFormFields()
				.stream()
				.map(FormField::new)
				.collect(Collectors.toList()))
			.build();
	}

	@Override
	public void submitForm(final List<FormSubmission> submittedFields, final String taskId) {
		log.info("Submitting form for task with id: {} and fields: {}", taskId, submittedFields);
		final Map<String, Object> fieldsMap = submittedFields.stream()
			.collect(HashMap::new, (map, field) -> map.put(field.getFieldId(), field.getValue()), HashMap::putAll);
		formService.submitTaskForm(taskId, fieldsMap);
	}

	@Override
	public void claimTask(final String taskId) {
		final var task = taskService.createTaskQuery().taskId(taskId).singleResult();
		final var user = runtimeService.getVariable(task.getProcessInstanceId(), "username").toString();
		log.info("User {} is claiming task with id: {}", user, taskId);
		taskService.claim(taskId, user);
	}

	@Override
	public void completeTask(final String taskId) {
		log.info("Completing task with id: {}", taskId);
		taskService.complete(taskId);
	}

	@Override
	public List<Task> getActiveUserTasks(final String username) {
		log.info("Finding user tasks for username: {}", username);
		return getTaskQuery(username)
			.list()
			.stream()
			.map(t -> Task.builder()
				.id(t.getId())
				.name(t.getName())
				.assignee(t.getAssignee())
				.process(t.getProcessDefinitionId())
				.processInstanceId(t.getProcessInstanceId())
				.variables(runtimeService.getVariables(t.getExecutionId()))
				.build()
			)
			.collect(Collectors.toList());
	}

	@Override
	public Task getTaskById(final String taskId) {
		final var task = taskService.createTaskQuery().taskId(taskId).singleResult();
		final var taskFormData = formService.getTaskFormData(task.getId());
		return Task.builder()
			.id(task.getId())
			.name(task.getName())
			.process(task.getProcessDefinitionId())
			.processInstanceId(task.getProcessInstanceId())
			.formFields(taskFormData.getFormFields()
				.stream()
				.map(FormField::new)
				.collect(Collectors.toList()))
			.build();
	}

	private TaskQuery getTaskQuery(final String username) {
		return Role.ADMIN.name().equalsIgnoreCase(username) ?
			taskService.createTaskQuery().active() :
			taskService.createTaskQuery().taskAssignee(username).active();
	}
}
