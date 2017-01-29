package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rap.common.Messages;
import ru.rap.common.validators.PasswordValidator;
import ru.rap.common.validators.UsernameValidator;
import ru.rap.entities.UserEntity;
import ru.rap.models.UserModel;
import ru.rap.repositories.RoleRepository;
import ru.rap.repositories.UserRepository;

/**
 * Сервис работы с пользователями
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public class UserService implements IService<UserModel, UserEntity>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsernameValidator usernameValidator;

	@Autowired
	private PasswordValidator passwordValidator;

	/**
	 * Регистрация нового пользователя
	 */
	public int registerUser(String username, String password)
	{
		// валидация
		if (!usernameValidator.validate(username)) {
			return Messages.RES_USERNAME_INVALID;
		}

		if (!passwordValidator.validate(password)) {
			return Messages.RES_PASSWORD_INVALID;
		}

		// проверяю наличие такого же имени
		UserEntity user = userRepository.findByName(username);

		if (user != null) {
			return Messages.RES_USER_ALREADY_EXIST;
		}

		// хеширую пароль
		String hashPassword = passwordEncoder.encode(password);

		// пользователь не существует, создаем
		userRepository.save(
				new UserEntity()
						.setName(username)
						.setHashPassword(hashPassword)
		);

		return 0;
	}

	public UserModel getByUid(int id)
	{
		UserEntity e = userRepository.findOne(id);
		return toPojo(e);
	}

	/**
	 * Поиск сущности по имени пользователя
	 *
	 * @param username
	 * @return
	 */
	public UserEntity getByName(String username)
	{
		UserEntity u = userRepository.findByName(username);
		return u;
	}

	@Override
	public UserModel toPojo(UserEntity arg)
	{
		return new UserModel(
				arg.getId(),
				arg.getName(),
				arg.getAnsweredCount(),
				arg.getAttemptCount(),
				arg.getCreated(),
				arg.getUpdated()
		);
	}
}
