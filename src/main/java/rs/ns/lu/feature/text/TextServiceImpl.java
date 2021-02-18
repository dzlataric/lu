package rs.ns.lu.feature.text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javax.transaction.Transactional;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import rs.ns.lu.feature.users.TextEntity;
import rs.ns.lu.feature.users.UserRepository;
import rs.ns.lu.util.ConstantsUtil;

@Slf4j
@Component
@AllArgsConstructor
@Transactional
public class TextServiceImpl implements TextService {

	private static final String UPLOADED_COUNT = "uploaded";
	
	private final UserRepository userRepository;
	private final RuntimeService runtimeService;

	@Override
	@SneakyThrows
	public void upload(final MultipartFile file, final String processInstanceId, final Long userId, final Boolean addMore) {
		final var originalName = Optional.ofNullable(file.getOriginalFilename()).orElseThrow();
		final var filepath = Paths.get(originalName);
		try {
			file.transferTo(filepath);
			final var title = originalName.replace(ConstantsUtil.SUPPORTED_FILE_TYPE_EXTENSION, "");
			final var user = userRepository.findById(userId).orElseThrow();
			user.getTexts().add(TextEntity.builder().user(user).title(title).content(file.getBytes()).build());

			log.info("Uploading file {} for user {}", title, user.getUsername());

			userRepository.save(user);

			if (addMore != null) {
				runtimeService.setVariable(processInstanceId, UPLOADED_COUNT, ((int) runtimeService.getVariable(processInstanceId, UPLOADED_COUNT)) + 1);
				runtimeService.setVariable(processInstanceId, "addMore", addMore);
			}
			Files.delete(filepath);
		} catch (final IOException | RuntimeException e) {
			log.error("Error updating text with file content", e);
			throw e;
		}
	}
}
