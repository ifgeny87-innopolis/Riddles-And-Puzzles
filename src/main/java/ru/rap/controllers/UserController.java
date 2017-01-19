package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.models.UserModel;
import ru.rap.services.UserService;

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

	@Autowired
	private UserService userService;

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
		int resCode = userService.registerUser(regEmail, regPwd);

		if (resCode != 0) {
			// не удалось создать пользователя
			model.addAttribute("error_message", Messages.get(resCode));
			return PAGE_INDEX;
		}

		// чтобы пользователь не тратил время на вход, сразу авторизую его
		return PAGE_INDEX;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  ACTIONS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  STATIC METHODS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

//	/**
//	 * Получение пользователя по номеру сессии
//	 * В случае исключения не выполняет переход на страницу ошибки, вернет null
//	 *
//	 * @param sessionId Номер сессии
//	 * @return Авторизованный пользователь
//	 */
//	UserModel getUserAuth(String sessionId) throws DbConnectException, DaoException
//	{
//		UserModel user = userService.getUserAuth(sessionId);
//		return user;
//	}
}
