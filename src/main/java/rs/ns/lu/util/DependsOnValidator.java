package rs.ns.lu.util;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class DependsOnValidator implements FormFieldValidator {
	@Override
	public boolean validate(final Object submittedValue, final FormFieldValidatorContext validatorContext) {
		return true;
	}
}
