package rs.ns.lu.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantsUtil {

	public static final String REGISTRATION_PROCESS_KEY = "registration";
	public static final String EMAIL_FORM_TYPE = "email";
	public static final String FILE_FORM_TYPE = "file";
	public static final String PASSWORD_FORM_TYPE = "password";
	public static final String CLIENT_VERIFICATION_URL = "http://localhost:4200/verify/";
	public static final String ACTIVATION_CODE_DELIMITER = ":";
	public static final String SUPPORTED_FILE_TYPE_EXTENSION = ".pdf";

}
