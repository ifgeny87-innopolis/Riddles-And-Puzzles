package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rap.common.Messages;
import ru.rap.common.validators.PasswordValidator;
import ru.rap.common.validators.UsernameValidator;
import ru.rap.dao.RoleDao;
import ru.rap.dao.UserDao;
import ru.rap.entities.UserEntity;
import ru.rap.models.UserModel;

import java.util.Collection;
import java.util.UUID;

/**
 * Сервис работы с пользователями
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public class UserService extends BaseService<UserModel>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

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
		UserEntity user = userDao.selectOneBy("name", username);

		if (user != null) {
			return Messages.RES_USER_ALREADY_EXIST;
		}

		// хеширую пароль
		String hashPassword = passwordEncoder.encode(password);

		// пользователь не существует, создаем
		userDao.insert(new UserEntity()
				.setName(username)
				.setHashPassword(hashPassword));

		return 0;
	}

	/**
	 * Возвращает модель для сущности
	 *
	 * @param e
	 * @return
	 */
	public UserModel getModel(UserEntity e)
	{
		return new UserModel(e.getId(), e.getName(), e.getAnsweredCount(), e.getAttemptCount());
	}

	public UserModel getByUid(UUID uid)
	{
		UserEntity e = userDao.selectOne("id", uid);
		return getModel(e);
	}

	/**
	 * Поиск сущности по имени пользователя
	 *
	 * @param username
	 * @return
	 */
	public UserEntity getByName(String username)
	{
		return userDao.selectOneBy("name", username);
	}

	public Collection<? extends GrantedAuthority> getUserRoles(UserEntity user)
	{
		return roleDao.getRolesByUser(user);
	}
}
