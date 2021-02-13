package rs.ns.lu.api;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	private String id;
	private String name;
	private String assignee;
	private String process;
	private String processInstanceId;
	private Map<String, Object> variables;
	private List<FormField> formFields;

}
