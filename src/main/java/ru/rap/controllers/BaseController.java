package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static ru.rap.common.PageList.ERROR_PAGE_JSP;

/**
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
public abstract class BaseController extends HttpServlet
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	// Параметры запроса
	protected HttpServletRequest req;   // request
	protected HttpServletResponse res;  // response
	protected String sessionId;         // номер сессии
	protected User authUser;            // текущий авторизованный юзер
	protected String path;              // корень приложения

	/**
	 * Инициализатор запрос
	 * Запоминает Request и Response чтобы потом удобней было к ним обращаться.
	 * Также запоминает sessionId, потому что с ним придется много работать.
	 *
	 * !!! Важно:
	 * Если вызов не для контроллера класса UserController, то проверяет авторизацию.
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	private boolean init(HttpServletRequest req, HttpServletResponse res) throws DbConnectException, DaoException
	{
		// чтобы получить UTF-8 из request
		// работает только для POST данных
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Сорвалась настройка кодировки для объекта Request:\n" + e.getMessage(), e);
		}

		this.req = req;
		this.res = res;
		this.sessionId = req.getRequestedSessionId();

		// корень приложения
		req.setAttribute("PATH", this.path = req.getContextPath());

		// если это не класс UserController, проверка авторизации
		if (this.getClass() != UserController.class) {
			if (!UserController.isUserAuth(sessionId)) {
				forwardError(403, Messages.ERR_ACCESS_DENIED);
				return false;
			} else {
				// запоминаю авторизованного
				this.authUser = UserController.getUserAuth(sessionId);
				// запишу сразу в атрибуты
				req.setAttribute("authUser", authUser);
			}
		}
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try {
			if (init(req, res))
				doGet();
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась обработка GET запроса:\n" + e.getMessage(), e);
			forwardError(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try {
			if (init(req, res))
				doPost();
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась обработка POST запроса:\n" + e.getMessage(), e);
			forwardError(e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  COMMON
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * По быстрому читает параметр
	 *
	 * @param name
	 * @return
	 */
	protected String param(String name)
	{
		return req.getParameter(name);
	}

	/**
	 * Redirect на другой url
	 *
	 * @param url
	 */
	protected void redirect(String url)
	{
		try {
			res.sendRedirect(url);
		} catch (IOException e) {
			log.error("[*] Очень редкая ошибка! Ловите ее! Вот причина:\n" + e.getMessage(), e);
			// что-то случилось, и даже пользователю не сообщить
			// Халк грустный :(
		}
	}

	/**
	 * Forward на другой url
	 *
	 * @param url
	 */
	protected void forward(String url)
	{
		try {
			req.getRequestDispatcher(url).forward(req, res);
		} catch (ServletException | IOException e) {
			log.error("[*] Еще одна редкая ошибка! А причина вот какая:\n" + e.getMessage(), e);
			// что-то случилось, и даже пользователю не сообщить
			// Халк грустный :(
		}
	}

	/**
	 * Выполняет forward на страницу ошибки
	 *
	 * @param code
	 * @param message
	 */
	protected void forwardError(int code, String message)
	{
		res.setStatus(code);
		req.setAttribute("error_code", code);
		req.setAttribute("error_message", message);

		forward(ERROR_PAGE_JSP);
	}

	/**
	 * Такой же метод, но умеет определяеть текст ошибки по индексу
	 *
	 * @param code
	 * @param errorIndex
	 */
	protected void forwardError(int code, int errorIndex)
	{
		forwardError(code, Messages.get(errorIndex));
	}

	/**
	 * Forward на страницу ошибки по типу исключения
	 *
	 * @param e
	 */
	protected void forwardError(Exception e)
	{
		if (e instanceof DbConnectException) {
			forwardError(500, Messages.ERR_DATABASE_CONNECTION);
		} else if (e instanceof DaoException) {
			forwardError(500, Messages.ERR_DATABASE_QUERY_EXECUTION);
		} else{
			forwardError(500, Messages.ERR_SERVER_CATCH);
		}
	}

	/**
	 * Преопределенный метод перехода на страницу ошибки:
	 * -- "Вызван неизвестный метод" --
	 *
	 * @param cmd
	 */
	protected void forwardErrorUnknownMethod(String cmd)
	{
		forwardError(405, Messages.format(Messages.ERR_UNKNOWN_METHOD, cmd));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  ABSTRACT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	protected abstract void doPost();

	protected abstract void doGet();
}
