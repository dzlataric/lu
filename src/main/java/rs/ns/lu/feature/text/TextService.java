package rs.ns.lu.feature.text;

import org.springframework.web.multipart.MultipartFile;

public interface TextService {

	void upload(MultipartFile file, String processInstanceId, Long userId, Boolean addMore);

}
