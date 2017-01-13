package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.models.User;
import ru.rap.services.UserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.rap.common.PageList.*;

/**
 * Сервлет-контроллер для работы с пользователями
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
@Controller
@RequestMapping(PAGE_USER)
public class UserController extends BaseController
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	// services
	private static final UserService userService = UserService.getInstance();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  CONTROLLER METHODS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(
			@RequestParam String regEmail,
			@RequestParam String regPwd
	)
	{
		// пробую создать нового пользователя
		int resCode;
		try {
			resCode = userService.registerUser(regEmail, regPwd);
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			return reportAndForwardError(e);
		}

		if (resCode != 0) {
			// не удалось создать пользователя
			model.addAttribute("error_message", Messages.get(resCode));
			return PAGE_INDEX;
		}

		// чтобы пользователь не тратил время на вход, сразу авторизую его
		return doAuth(regEmail, regPwd);
	}

	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public String auth(
			@RequestParam String authEmail,
			@RequestParam String authPwd,
			HttpServletResponse response,
			Model model
	)
	{
		this.response = response;
		this.model = model;
		return doAuth(authEmail, authPwd);
	}

	@RequestMapping("exit")
	public String exit()
	{
		userService.exitUser(getSessionId());

		// сбрасываю sessionId
		request.changeSessionId();

		// вышел или не вышел, не важно, отправляю его на домашнюю
		return PAGE_INDEX;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  ACTIONS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Авторизация пользователя в ИС
	 *
	 * @param authEmail email пользователя
	 * @param authPwd   RAW пароль пользователя
	 */
	private String doAuth(String authEmail, String authPwd)
	{
		// пробую авторизовать пользователя
		int resCode;
		try {
			resCode = userService.authUser(getSessionId(), authEmail, authPwd);
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			return reportAndForwardError(e);
		}

		if (resCode != 0) {
			// не удалось авторизоваться
			model.addAttribute("error_message", Messages.get(resCode));
			return PAGE_INDEX;
		}

		// пользователь авторизован, отправляю его на страницу списка задач
		return redirectTo(PAGE_RIDDLES);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  STATIC METHODS
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
		boolean flag = userService.isUserAuth(sessionId);
		return flag;
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
		User user = userService.getUserAuth(sessionId);
		return user;
	}
}
