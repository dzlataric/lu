package rs.ns.lu.util;

import org.camunda.bpm.engine.impl.form.type.StringFormType;

public class FileFormType extends StringFormType {

    @Override
    public String getName() {
        return ConstantsUtil.FILE_FORM_TYPE;
    }

}
