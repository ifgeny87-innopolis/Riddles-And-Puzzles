package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.libraries.PagerLibrary;
import ru.rap.models.Riddle;
import ru.rap.services.RiddleService;

import javax.servlet.annotation.WebServlet;
import java.util.List;

import static ru.rap.common.Config.ITEMS_PER_PAGE;
import static ru.rap.common.PageList.WORKSHOP_JSP;

/**
 * Сервлет-контроллер для работы с загадками пользователя
 *
 * Created in project RiddlesAndPuzzles in 27.12.2016
 */
@WebServlet(urlPatterns = {"/workshop"})
public class WorkshopController extends BaseController
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(WorkshopController.class);

	// Services
	private static RiddleService riddleService = RiddleService.getInstance();

	@Override
	protected void doPost()
	{
		doGet();
	}

	@Override
	protected void doGet()
	{
		int count;
		try {
			// получаю количество загадок не текущего пользователя
			count = riddleService.getCountOfUser(authUser.getId());
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалось получение количества:\n" + e.getMessage(), e);
			forwardError(e);
			return;
		}

		// какой номер страницы
		int pageIndex = PagerLibrary.getWorkshopPageIndex(req);
		int pageCount = (int) Math.ceil(1. * count / ITEMS_PER_PAGE);
		pageIndex = Math.max(1, Math.min(pageCount, pageIndex));

		// получаю список загадок не текущего пользователя
		List<Riddle> riddles;
		try {
			riddles = riddleService.getListOfUser(authUser.getId(),
					(pageIndex - 1) * ITEMS_PER_PAGE,
					ITEMS_PER_PAGE);
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалось получение загадок:\n" + e.getMessage(), e);
			forwardError(e);
			return;
		}

		req.setAttribute("pageCount", pageCount);
		req.setAttribute("pageIndex", pageIndex);
		req.setAttribute("riddles", riddles);

		forward(WORKSHOP_JSP);
	}
}
