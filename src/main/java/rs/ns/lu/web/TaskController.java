package rs.ns.lu.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import rs.ns.lu.feature.tasks.TaskService;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

	private final TaskService taskService;

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
