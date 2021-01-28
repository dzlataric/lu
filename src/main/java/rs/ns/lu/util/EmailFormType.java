package rs.ns.lu.util;

import org.camunda.bpm.engine.impl.form.type.StringFormType;

public class EmailFormType extends StringFormType {

    @Override
    public String getName() {
        return ProcessUtil.EMAIL_FORM_TYPE;
    }
}
