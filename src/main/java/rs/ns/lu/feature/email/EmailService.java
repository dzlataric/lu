package rs.ns.lu.feature.email;

import rs.ns.lu.feature.users.UserEntity;

public interface EmailService {

	void sendUserConfirmation(String processInstanceId, UserEntity user);

	void sendUserRejected(String address);

	void sendUserAccepted(String address);

	void sendUserNeedsMoreTexts(String address);

}
