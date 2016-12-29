package ru.rap.restapi;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
abstract class BaseApi extends HttpServlet {

	// logger
	private static final Logger log = LoggerFactory.getLogger(BaseApi.class);

	private static Gson gson = new Gson();

	/**
	 * Конвертирует args в Json строку и пишет в стрим Response
	 *
	 * @param res  Response
	 * @param args Объекты для отправки
	 * @throws IOException Возможна ошибка при записи в стрим
	 */
	protected void sendJson(HttpServletResponse res, Object... args) throws IOException {
		res.setHeader("Content-Type", "application/json; charset=UTF-8");

		String json = (args.length == 1)
				? gson.toJson(args[0])
				: gson.toJson(args);

		// FIXME: 24.12.2016 если нужно в UTF8 конвертировать, то можно так
//		res.getWriter().print(new String(json.getBytes("UTF-8")));
		res.getWriter().print(json);
	}
}
