package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.Messages;
import ru.rap.common.PageList;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.common.exceptions.DaoException;
import ru.rap.models.User;
import ru.rap.services.UserService;

import javax.servlet.annotation.WebServlet;

import static ru.rap.common.PageList.INDEX_JSP;

/**
 * Сервлет-контроллер для работы с пользователями
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
@WebServlet(urlPatterns = {"/user"})
public class UserController extends BaseController
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	// Service
	private static UserService userService = UserService.getInstance();

	@Override
	protected void doPost()
	{
		// в queryString хранится название метода
		String cmd = req.getParameter("method");
		if ("register".equals(cmd)) {
			// пользователь желает зарегистрироваться
			doRegister(param("regEmail"), param("regPwd"));
		} else if ("auth".equals(cmd)) {
			// пользователь желает авторизоваться
			doAuth(param("authEmail"), param("authPwd"));
		} else {
			// неизвестный метод
			forwardErrorUnknownMethod(cmd);
		}
	}

	@Override
	protected void doGet()
	{
		// в queryString хранится команда
		String cmd = req.getParameter("method");
		if ("exit".equals(cmd)) {
			// пользователь желает выйти из системы
			doExit();
		} else {// неизвестный метод
			forwardErrorUnknownMethod(cmd);
		}
	}

	/**
	 * Регистрация нового пользователя в ИС
	 *
	 * @param name     Имя нового пользователя
	 * @param password RAW Пароль нового пользователя
	 */
	private void doRegister(String name, String password)
	{
		// пробую создать нового пользователя
		int result;
		try {
			result = userService.registerUser(name, password);
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			forwardError(500, (e instanceof DaoException)
					? Messages.ERR_SERVER_CATCH
					: Messages.ERR_DATABASE_CONNECTION);
			return;
		}

		if (result != 0) {
			// не удалось создать пользователя
			req.setAttribute("register_err", Messages.get(result));
			forward(INDEX_JSP);
			return;
		}

		// чтобы пользователь не тратил время на вход, сразу авторизую его
		doAuth(name, password);
	}

	/**
	 * Авторизация пользователя в ИС
	 *
	 * @param name     Имя пользователя
	 * @param password RAW Пароль пользователя
	 */
	private void doAuth(String name, String password)
	{
		// пробую авторизовать пользователя
		int result;
		try {
			result = userService.authUser(sessionId, name, password);
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			forwardError(e);
			return;
		}

		if (result != 0) {
			// не удалось авторизоваться
			req.setAttribute("register_err", Messages.get(result));
			forward(INDEX_JSP);
			return;
		}

		// else (result == 0)
		// пользователь авторизован, отправляю его на страницу списка задач
		redirect(path + PageList.RIDDLES);
	}

	/**
	 * Выход пользователя
	 */
	private void doExit()
	{
		userService.exitUser(sessionId);

		// сбрасываю sessionId
		req.changeSessionId();

		// вышел или не вышел, не важно, отправляю его на домашнюю
		redirect(path + INDEX_JSP);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  STATIC METHODS HERE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Проверка авторизации по номеру сессии
	 * [!] Запрос к базе не выполняется
	 *
	 * @param sessionId Номер сессии
	 * @return Флаг авторизации
	 */
	public static boolean isUserAuth(String sessionId)
	{
		return userService.isUserAuth(sessionId);
	}

	/**
	 * Получение пользователя по номеру сессии
	 * В случае исключения не выполняет переход на страницу ошибки, вернет null
	 *
	 * @param sessionId Номер сессии
	 * @return Авторизованный пользователь
	 */
	static User getUserAuth(String sessionId) throws DbConnectException, DaoException
	{
		return userService.getUserAuth(sessionId);
	}
}
