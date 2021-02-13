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

	private final UserRepository userRepository;
	private final RuntimeService runtimeService;

	@Override
	@SneakyThrows
	public void upload(final MultipartFile file, final String processInstanceId, final Long userId, final boolean addMore) {
		try {
			final var originalName = Optional.ofNullable(file.getOriginalFilename()).orElseThrow();
			final var filepath = Paths.get(originalName);
			file.transferTo(filepath);
			final var title = originalName.replace(ConstantsUtil.SUPPORTED_FILE_TYPE_EXTENSION, "");
			final var user = userRepository.findById(userId).orElseThrow();
			final var oldSize = user.getTexts().size();
			log.info("Old size: {}", oldSize);
			user.getTexts().add(TextEntity.builder().user(user).title(title).content(file.getBytes()).build());

			log.info("Uploading file {} for user {}", title, user.getUsername());

			userRepository.save(user);
			
			final var newSize = user.getTexts().size();
			log.info("New size: {}", newSize);

			runtimeService.setVariable(processInstanceId, "uploaded", user.getTexts().size());
			runtimeService.setVariable(processInstanceId, "addMore", addMore);
			Files.delete(filepath);
		} catch (final IOException | RuntimeException e) {
			log.error("Error updating text with file content", e);
			throw e;
		}
	}
}
