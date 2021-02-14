package rs.ns.lu.feature.tasks;

import java.util.List;

import rs.ns.lu.api.FormSubmission;
import rs.ns.lu.api.Role;
import rs.ns.lu.api.Task;

public interface TaskService {

	Task startProcessAndGetFormFields(Role role, String username);

	void submitForm(List<FormSubmission> submittedFields, String taskId);

	void claimTask(String taskId);

	void completeTask(String taskId);

	List<Task> getActiveUserTasks(String username);

	Task getTaskById(String taskId);
}
