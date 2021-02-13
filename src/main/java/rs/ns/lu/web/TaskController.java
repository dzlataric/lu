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

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

	private final TaskService taskService;

	@GetMapping(path = "/{taskId}")
	public ResponseEntity<Task> get(@PathVariable final String taskId) {
		return ResponseEntity.ok().body(taskService.getTaskById(taskId));
	}

	@PostMapping(value = "/submit/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> submitForm(@RequestBody final List<FormSubmission> submittedFields, @PathVariable final String taskId) {
		taskService.submitForm(submittedFields, taskId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/claim/{taskId}")
	public ResponseEntity<Void> claim(@PathVariable final String taskId) {
		taskService.claimTask(taskId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/complete/{taskId}")
	public ResponseEntity<Void> complete(@PathVariable final String taskId) {
		taskService.completeTask(taskId);
		return ResponseEntity.ok().build();
	}

}
