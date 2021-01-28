package rs.ns.lu.api;

import java.util.List;

import org.camunda.bpm.engine.form.FormFieldValidationConstraint;
import org.camunda.bpm.engine.form.FormType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormField {

	private String id;
	private String label;
	private FormType type;
	private Object value;
	private List<FormFieldValidationConstraint> validationConstraints;

	public FormField(final org.camunda.bpm.engine.form.FormField formField) {
		this.id = formField.getId();
		this.label = formField.getLabel();
		this.type = formField.getType();
		this.value = formField.getDefaultValue() != null ? formField.getDefaultValue() : formField.getValue().getValue();
		this.validationConstraints = formField.getValidationConstraints();
	}

}
