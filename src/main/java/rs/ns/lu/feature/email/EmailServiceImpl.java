package rs.ns.lu.feature.email;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.feature.users.UserEntity;
import rs.ns.lu.util.ConstantsUtil;

@Slf4j
@Component
class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.properties.mail.sender}")
	private String address;

	@Value("${spring.mail.properties.mail.sender.alias}")
	private String alias;

	private final JavaMailSender mailSender;

	public EmailServiceImpl(final JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	@SneakyThrows
	public void sendUserConfirmation(final String processInstanceId, final UserEntity user) {
		log.info("Sending mail to user: {}", user.getUsername());
		try {
			final MimeMessage mail = mailSender.createMimeMessage();

			final var activationCode = getActivationCode(processInstanceId, user.getUsername());
			constructMailMessage(mail, "Account activation", user.getEmail(),
				"To activate your account please " + "<a href=\"" + activationCode + "\">click here.</a><br><br>");

			mailSender.send(mail);
			log.info("Email sent to user {} with email {}. Activation code: {}", user.getUsername(), user.getEmail(), activationCode);
		} catch (final Exception e) {
			log.error("Error sending email to user {} with email {}", user.getUsername(), user.getEmail());
			throw e;
		}
	}

	@Override
	@SneakyThrows
	public void sendUserRejected(final String address) {
		log.info("Sending rejection mail to: {}", address);
		try {
			final MimeMessage mail = mailSender.createMimeMessage();
			constructMailMessage(mail, "Rejected", address, "Registration rejected!");
			mailSender.send(mail);
			log.info("User rejection mail successfully sent to {}", address);
		} catch (final Exception e) {
			log.error("Error sending rejection mail to address {}", address, e);
			throw e;
		}
	}

	@Override
	@SneakyThrows
	public void sendUserAccepted(final String address) {
		log.info("Sending accepted mail to: {}", address);
		try {
			final MimeMessage mail = mailSender.createMimeMessage();
			constructMailMessage(mail, "Accepted", address, "Registration accepted!");
			mailSender.send(mail);
			log.info("User accepted mail successfully sent to {}", address);
		} catch (final Exception e) {
			log.error("Error sending rejection mail to address {}", address, e);
			throw e;
		}
	}

	@Override
	@SneakyThrows
	public void sendUserNeedsMoreTexts(final String address) {
		log.info("Sending needs more texts mail to: {}", address);
		try {
			final MimeMessage mail = mailSender.createMimeMessage();
			constructMailMessage(mail, "Upload more texts", address, "Registration to be determine after another round of voting!");
			mailSender.send(mail);
			log.info("User needs more texts mail successfully sent to {}", address);
		} catch (final Exception e) {
			log.error("Error sending needs more texts mail to address {}", address, e);
			throw e;
		}
	}

	private void constructMailMessage(final MimeMessage mail, final String subject, final String to, final String text)
		throws MessagingException, UnsupportedEncodingException {
		final MimeMessageHelper helper = new MimeMessageHelper(mail, false, "utf-8");
		helper.setFrom(new InternetAddress(address, alias));
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
	}

	private String getActivationCode(final String processInstanceId, final String username) {
		return ConstantsUtil.CLIENT_VERIFICATION_URL
			.concat(Base64.getEncoder().withoutPadding()
				.encodeToString(processInstanceId.concat(ConstantsUtil.ACTIVATION_CODE_DELIMITER).concat(username).getBytes()));
	}
}
