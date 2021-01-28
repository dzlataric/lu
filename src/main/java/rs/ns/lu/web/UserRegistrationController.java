package rs.ns.lu.web;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import rs.ns.lu.api.FormSubmission;
import rs.ns.lu.api.Task;
import rs.ns.lu.feature.tasks.TaskService;
import rs.ns.lu.feature.users.UserService;
import rs.ns.lu.util.ProcessUtil;

@RestController
@AllArgsConstructor
@RequestMapping("/registration")
public class UserRegistrationController {

	private final TaskService taskService;
	private final UserService userService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> startUserRegistration() {
		return ResponseEntity.ok(taskService.startProcessAndGetFormFields(ProcessUtil.REGISTRATION_PROCESS_KEY, null));
	}

	@PostMapping(value = "/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> submitForm(@RequestBody final List<FormSubmission> submittedFields, @PathVariable final String taskId) {
		taskService.submitForm(submittedFields, taskId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(value = "/verify/{code}")
	public ResponseEntity<Void> verifyUser(@PathVariable final String code) {
		userService.verifyUser(code);
		return ResponseEntity.ok().build();
	}

}
