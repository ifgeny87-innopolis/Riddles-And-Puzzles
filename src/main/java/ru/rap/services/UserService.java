package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.dao.UserDao;
import ru.rap.libraries.HashLibrary;
import ru.rap.models.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис работы с пользователями
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public class UserService extends BaseService<User>
{
	// Logger
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Override
	Logger getLogger() { return log;}

	// для дебага в основном, нужно ли выполнять авто-вход
	private static boolean ENABLE_AUTO_AUTH = false;

	// Singleton
	private UserService() {}

	private static class LazySingleton
	{
		private static UserService instance = new UserService();
	}

	public static UserService getInstance()
	{
		return LazySingleton.instance;
	}

	// DAO
	private static UserDao dao = UserDao.getInstance();

	@Override
	UserDao getDao() { return dao;}

	// Карта авторизации
	private static Map<String, UUID> authUserMap = new HashMap<>();

	/**
	 * Получает существующий объект или создает новый
	 * Но все равно возвращает объект
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	/*public User getOrCreate(String name) throws DaoException, DbConnectException
	{
		User user = dao.selectOneBy("name", name);
		if (user != null)
			return user;

		// объект не найден, пробуем создать
		return !save(new User(name))
				? null
				: dao.selectOneBy("name", name);
	}*/

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Регистрация нового пользователя
	 *
	 * @param name
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public int registerUser(String name, String password) throws DaoException, DbConnectException
	{
		User user = dao.selectOne("WHERE name=?", name);

		if (user != null) {
			return Messages.RES_USER_ALREADY_EXIST;
		}

		// хеширую пароль
		String hash_password = HashLibrary.toMd5(password, name);

		// пользователь не существует, создаем
		boolean ok;
		try {
			ok = dao.insert(new User(null, name, null,
					hash_password, 0, 0));
			dao.commit();
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			// делаю откат
			dao.rollback();
			throw e;
		}

		return (ok) ? 0 : -1;
	}

	/**
	 * Выполняет авторизацию пользвоателя
	 *
	 * @param sessionId
	 * @param name
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public int authUser(String sessionId, String name, String password) throws DaoException, DbConnectException
	{
		// хеширую пароль
		String hash_password = HashLibrary.toMd5(password, name);

		// попробую найти пользователя
		User user = dao.selectOne("WHERE name=? AND hash_password=?", name, hash_password);

		if (user == null) {
			return Messages.RES_USER_OR_PASSWORD_WRONG;
		}

		// такой пользователь есть, авторизуем
		// просто по сессии маппим пользователя
		authUserMap.put(sessionId, user.getId());

		return 0;
	}

	public void exitUser(String sessionId)
	{
		// TODO: 28.12.2016 ключ для автовхода
		// если в рамках сессии пользователь вышел, то автовход выполняться больше не будет
		ENABLE_AUTO_AUTH = false;

		if (authUserMap.containsKey(sessionId))
			authUserMap.remove(sessionId);
	}

	/**
	 * Проверяет, существует ли запись об авторизации для пользоваеля
	 *
	 * @param sessionId
	 * @return
	 */
	public boolean isUserAuth(String sessionId)
	{
		// TODO: 28.12.2016 исключить автовход
		// анормальное поведение - авто-вход
		if (!authUserMap.containsKey(sessionId) && ENABLE_AUTO_AUTH) {
			String autoUserId = "AE7F5659-CCE6-11E6-9652-3C077170ECDC";
			authUserMap.put(sessionId, UUID.fromString(autoUserId));
		}

		// нормальное поведение
		return authUserMap.containsKey(sessionId);
	}

	/**
	 * Возвращает авторизованного пользователя или ll
	 * Если ключ в карте авторизации есть, но пользователя уже нет, удаляет ключ из карты
	 *
	 * @param sessionId
	 * @return
	 * @throws SQLException
	 */
	public User getUserAuth(String sessionId) throws DaoException, DbConnectException
	{
		UUID id = authUserMap.containsKey(sessionId)
				? authUserMap.get(sessionId)
				: null;

		if (id == null)
			return null;

		User user = dao.selectOneBy("id", id.toString());

		// сервис должен отслеживать авторизацию
		// если сейчас user == null, значит юзер был удален после авторизации
		// поэтому нужно удалить ключ из карты авторизации
		if (user == null) {
			authUserMap.remove(sessionId);
		}

		return user;
	}

	/**
	 * Увеличивает количество попыток, а если указан флаг rightsToo,
	 * то увеличивает количество решеных задач
	 *
	 * @param u         Пользователь
	 * @param rightsToo Нужно ли увеличить счетчик решеных задач
	 * @return 0 или номер ошибки
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public int incTries(User u, boolean rightsToo) throws DbConnectException, DaoException
	{
		int answer_count = u.getAnswerCount() + (rightsToo ? 1 : 0);
		int try_count = u.getTryCount() + 1;

		return dao.update(new User(u.getId(), u.getName(), u.getBirth(), u.getHashPassword(), answer_count, try_count)) ? 0 : -1;
	}

}
