package rs.ns.lu.feature.email;

import rs.ns.lu.feature.users.UserEntity;

public interface EmailService {

	void sendUserConfirmation(String processInstanceId, UserEntity user);

}
