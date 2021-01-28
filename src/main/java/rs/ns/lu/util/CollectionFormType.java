package rs.ns.lu.util;

import java.util.Collection;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class CollectionFormType extends EnumFormType {

	public CollectionFormType(final Map<String, String> values) {
		super(values);
	}

	@Override
	public String getName() {
		return "collection";
	}

	@Override
	protected void validateValue(final Object value) {
		final Collection<?> collection = (Collection<?>) value;
		collection.forEach(o -> {
			if (this.values != null && !this.values.containsKey(o)) {
				throw new ProcessEngineException("Invalid value for collection form property: " + value);
			}
		});
	}

	@Override
	public TypedValue convertValue(final TypedValue propertyValue) {
		final Object value = propertyValue.getValue();
		if (value != null && !(value instanceof Collection)) {
			throw new ProcessEngineException("Value '" + value + "' is not of type Collection.");
		}
		return Variables.objectValue(value, propertyValue.isTransient()).create();
	}
}
