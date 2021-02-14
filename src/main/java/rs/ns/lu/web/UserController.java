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
import rs.ns.lu.api.Role;
import rs.ns.lu.api.Task;
import rs.ns.lu.api.User;
import rs.ns.lu.feature.tasks.TaskService;
import rs.ns.lu.feature.users.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final TaskService taskService;
	private final UserService userService;

	@GetMapping(value = "/registration/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> startUserRegistration(@PathVariable("role") final Role role) {
		return ResponseEntity.ok(taskService.startProcessAndGetFormFields(role, null));
	}

	@PostMapping(value = "/registration/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> submitForm(@RequestBody final List<FormSubmission> submittedFields, @PathVariable final String taskId) {
		taskService.submitForm(submittedFields, taskId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(value = "/registration/verify/{code}")
	public ResponseEntity<Void> verifyUser(@PathVariable final String code) {
		userService.verifyUser(code);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> login(@RequestBody final User loginUser) {
		return ResponseEntity.ok(userService.loginAndEnrich(loginUser));
	}

	@GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Task>> getActiveTasks(@PathVariable final String username) {
		return ResponseEntity.ok(taskService.getActiveUserTasks(username));
	}

}
