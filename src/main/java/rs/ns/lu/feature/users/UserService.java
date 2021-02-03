package rs.ns.lu.feature.users;

import java.util.Optional;

import rs.ns.lu.api.User;

public interface UserService {

	User loginAndEnrich(User loginUser);

	Optional<UserEntity> findByUsername(String username);

	void save(UserEntity user);

	void verifyUser(String verificationCode);
}
