package ru.rap.restapi;

import ru.rap.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
@WebServlet(urlPatterns = {"/api/user/*"})
public class UserApi extends BaseApi {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// передал ли пользователь имя?
		String name = req.getParameter("name");
		if (name == null || name.length() == 0) {
			sendJson(res, JsonResult.genError("Мне нужно твое имя", JsonResult.ErrorLevel.info));
			return;
		}

		// пробую получить или создать пользователя
		User user = null;

		// если пользователь не вернулся, так и пишу
		if (user == null) {
			sendJson(res, JsonResult.genError("Вжжух... Что-то сломалось"));
			return;
		}

		// иначе вывожу данные о пользователе
		sendJson(res, JsonResult.genOkResult(user.getMap()));
	}
}
