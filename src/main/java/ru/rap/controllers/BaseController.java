package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.models.User;
import ru.rap.policies.RapPrincipal;
import ru.rap.policies.RapUserDetails;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.rap.common.PageList.PAGE_ERROR;

/**
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
public abstract class BaseController
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	UserController userController;

	private String path;

	HttpServletRequest request;

	HttpServletResponse response;

	Model model;

	private String sessionId;

	String getSessionId()
	{
		return sessionId;
	}

	/**
	 * Инициализатор запроса
	 * Запоминает Request и Response чтобы потом удобней было к ним обращаться.
	 * Также запоминает sessionId, потому что с ним придется много работать.
	 *
	 * !!! Важно:
	 * Если вызов не для контроллера класса UserController, то проверяет авторизацию.
	 */
	@ModelAttribute
	public void init(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{
		this.request = request;
		this.response = response;
		this.model = model;
		this.sessionId = request.getRequestedSessionId();

		// чтобы получить UTF-8 из request
		// работает только для POST данных
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Сорвалась настройка кодировки для объекта Request:\n" + e.getMessage(), e);
		}

		// корень приложения
		request.setAttribute("PATH", this.path = request.getContextPath());
	}

	protected final String redirectTo(String page_name)
	{
		return "redirect:/" + page_name;
	}

	protected final void reportError(int statusCode, int errorCode)
	{
		model.addAttribute("status_code", statusCode);
		model.addAttribute("error_message", Messages.get(errorCode));
	}

	protected final String reportAndForwardError(Exception e)
	{
		model.addAttribute("error_code", 500);

		int code;

		if (e instanceof DaoException)
			code = Messages.ERR_SERVER_CATCH;
		else if (e instanceof DbConnectException)
			code = Messages.ERR_DATABASE_CONNECTION;
		else
			code = Messages.ERR_SERVER_CATCH;

		reportError(500, code);

		return PAGE_ERROR;
	}

	/**
	 * UserDetails для текущего авторизованного пользователя
	 * @return
	 */
	public RapUserDetails getPrincipalDetails()
	{
		UsernamePasswordAuthenticationToken principalToken = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();

		return (RapUserDetails)principalToken.getPrincipal();
	}

	/**
	 * Список ролей авторизованного пользователя
	 * @return
	 */
	public Collection<GrantedAuthority> getPrincipalAuthorities() {
		UsernamePasswordAuthenticationToken principalToken = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();

		return principalToken.getAuthorities();
	}

	/**
	 * Ловушка для ошибок
	 * @param ex
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex)
	{
		Map<String, Object> model = new HashMap<>();
		model.put("exception_class", ex.getClass());
		model.put("exception_message", ex.getMessage());
		model.put("exception_trace", Arrays
				.stream(ex.getStackTrace())
				.map(t -> "call <b>" + t.getMethodName() + "</b> from <i>" + t.getFileName() + ":" + t.getLineNumber() + "</i>")
				.collect(Collectors.joining("<br/>"))
				.toString());
		return new ModelAndView("error", model);
	}
}
