package rs.ns.lu.feature.email;

import java.util.Base64;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.feature.users.UserEntity;

@Slf4j
@Component
@AllArgsConstructor
class EmailServiceImpl implements EmailService {

	private static final String CLIENT_URL = "http://localhost:4200";
	private static final String VERIFICATION_BASE_URL = "/verify/";

	private final JavaMailSender mailSender;

	@Override
	public void sendUserConfirmation(final String processInstanceId, final UserEntity user) {
		log.info("Sending mail to user: {}", user.getUsername());
		try {
			final MimeMessage mail = mailSender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(mail, false, "utf-8");

			helper.setTo(user.getEmail());
			helper.setSubject("Account activation");
			final String message = "To activate your account please " + "<a href=\"" + getActivationCode(processInstanceId) + "\">click here.</a><br><br>";
			helper.setText(message, true);
			mailSender.send(mail);

			log.info("Email sent to user {} with email {}", user.getUsername(), user.getEmail());
		} catch (final Exception e) {
			log.error("Error sending email to user {} with email {}", user.getUsername(), user.getEmail());
		}
	}

	private String getActivationCode(final String processInstanceId) {
		return CLIENT_URL.concat(VERIFICATION_BASE_URL).concat(Base64.getEncoder().withoutPadding().encodeToString(processInstanceId.getBytes()));
	}
}
