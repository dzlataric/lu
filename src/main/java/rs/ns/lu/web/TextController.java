package rs.ns.lu.web;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import rs.ns.lu.feature.text.TextService;

@RestController
@AllArgsConstructor
@RequestMapping("/text")
public class TextController {

	private final TextService textService;

	@PostMapping("/upload/{userId}/{processInstanceId}/{addMore}")
	public void uploadContent(@RequestParam("file") @NotNull final MultipartFile file, @PathVariable("userId") final Long userId,
		@PathVariable("processInstanceId") final String processInstanceId, @PathVariable("addMore") final Boolean addMore) {
		textService.upload(file, processInstanceId, userId, addMore);
	}

}
