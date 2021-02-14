package rs.ns.lu.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.context.annotation.Configuration;

import rs.ns.lu.util.CollectionFormType;
import rs.ns.lu.util.CollectionMinSizeValidator;
import rs.ns.lu.util.DependsOnValidator;
import rs.ns.lu.util.EmailFormType;
import rs.ns.lu.util.FileFormType;
import rs.ns.lu.util.PasswordFormType;

@Configuration
public class CustomFormFieldConfig extends AbstractProcessEnginePlugin {

	@Override
	public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
		if (processEngineConfiguration.getCustomFormTypes() == null) {
			processEngineConfiguration.setCustomFormTypes(new ArrayList<>());
		}
		if (processEngineConfiguration.getCustomFormFieldValidators() == null) {
			processEngineConfiguration.setCustomFormFieldValidators(new HashMap<>());
		}
		processEngineConfiguration.getCustomFormTypes().add(new CollectionFormType(new HashMap<>()));
		processEngineConfiguration.getCustomFormTypes().add(new EmailFormType());
		processEngineConfiguration.getCustomFormTypes().add(new PasswordFormType());
		processEngineConfiguration.getCustomFormTypes().add(new FileFormType());
		processEngineConfiguration.getCustomFormFieldValidators().put("minElements", CollectionMinSizeValidator.class);
		processEngineConfiguration.getCustomFormFieldValidators().put("dependsOn", DependsOnValidator.class);
	}

}
