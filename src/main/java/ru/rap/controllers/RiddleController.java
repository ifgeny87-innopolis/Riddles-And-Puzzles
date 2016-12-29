package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.libraries.PagerLibrary;
import ru.rap.models.Riddle;
import ru.rap.services.AnswerService;
import ru.rap.services.RiddleService;
import ru.rap.services.UserService;

import javax.servlet.annotation.WebServlet;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import static ru.rap.common.Config.ITEMS_PER_PAGE;
import static ru.rap.common.PageList.*;

/**
 * Сервлет-контроллер управления загадками
 *
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
@WebServlet(urlPatterns = {RIDDLES + "/*"})
public class RiddleController extends BaseController
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RiddleController.class);

	// Services
	private static RiddleService riddleService = RiddleService.getInstance();
	private static UserService userService = UserService.getInstance();
	private static AnswerService answerService = AnswerService.getInstance();

	@Override
	protected void doPost()
	{
		doGet();
	}

	@Override
	protected void doGet()
	{
		// определяю вызванное действие - окончание после шаблона запроса
		String action = req.getPathInfo();

		if (action == null || "/".equals(action)) doViewRiddles();
		else if (action.startsWith("/answer/")) doAnswerRiddle(action.substring(8));
		else if ("/create".equals(action)) doCreateRiddle();
		else if (action.startsWith("/edit/")) doEditRiddle(action.substring(6));
		else if (action.startsWith("/delete/")) doDeleteRiddle(action.substring(8));
		else forwardError(404, Messages.PAGE_NOT_FOUND);
	}

	/**
	 * Выводит список загадок
	 */
	private void doViewRiddles()
	{
		int count;
		try {
			// получаю количество загадок не текущего пользователя
			count = riddleService.getCountNotOfUser(authUser.getId());
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась выборка количества записей:\n" + e.getMessage(), e);
			forwardError(e);
			return;
		}

		// какой номер страницы
		int pageIndex = PagerLibrary.getRiddlesPageIndex(req);
		int pageCount = (int) Math.ceil(1. * count / ITEMS_PER_PAGE);
		pageIndex = Math.max(1, Math.min(pageCount, pageIndex));

		// получаю список загадок не текущего пользователя
		Map<Riddle, Timestamp> riddles;
		try {
			riddles = riddleService.getListNotOfUser(authUser.getId(),
					(pageIndex - 1) * ITEMS_PER_PAGE,
					ITEMS_PER_PAGE);
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалось получение списка:\n" + e.getMessage(), e);
			forwardError(e);
			return;
		}

		req.setAttribute("pageCount", pageCount);
		req.setAttribute("pageIndex", pageIndex);
		req.setAttribute("riddles", riddles);

		forward(RIDDLES_JSP);
	}

	/**
	 * Возвращает загадку по номеру
	 *
	 * @param uuid_text Номер загадки в тексте
	 * @return null или загадка
	 */
	private Riddle _getRiddle(String uuid_text)
	{
		try {
			return riddleService.getRiddle(UUID.fromString(uuid_text));
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась выборка загадки:\n" + e.getMessage(), e);
			forwardError(e);
			return null;
		}
	}

	/**
	 * Отгадывание выбранной загадки
	 */
	private void doAnswerRiddle(String uuid_text)
	{
		// получаю загадку
		Riddle riddle = _getRiddle(uuid_text);
		if (riddle == null) {
			req.setAttribute("error_message", "Загадка не существует. Возможно, украли инопланетяне.");
			forward(RIDDLES);
			return;
		}

		// проверка доступа к загадке
		if (riddle.getUserId().equals(authUser.getId())) {
			req.setAttribute("error_message", "Вы не можете отгадывать свои загадки.");
			forward(RIDDLES);
			return;
		}

		// если загадка уже была решена этим пользователей, то ее нельзя решать снова
		try {
			if (answerService.isAnswerRight(authUser.getId(), riddle.getId())) {
				req.setAttribute("error_message", "Загадка уже была отгадана вами. Ее нельзя отгадывать снова.");
				forward(RIDDLES);
				return;
			}
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась проверка решеной загадки:\n" + e.getMessage(), e);
			forwardError(e);
			return;
		}

		// поля загадки для frontend
		req.setAttribute("title", riddle.getTitle());
		req.setAttribute("text", riddle.getText());
		req.setAttribute("image", riddle.getImage());

		// если указан метод post, то пользователь пытается решить загадку
		if ("post".equalsIgnoreCase(req.getMethod())) {
			String answer = req.getParameter("answer")
					.trim().toLowerCase().replace("[\\s]{2,}", " ");

			int result;
			try {
				result = riddleService.answer(authUser, riddle, answer);
			} catch (DbConnectException | DaoException e) {
				log.error("Сорвалась проверка отгадки:\n" + e.getMessage(), e);
				forwardError(e);
				return;
			}

			if (result == 1) {
				req.setAttribute("success_messages", "Вы отгадали загадку");
				forward(RIDDLES);
				return;
			} else {
				req.setAttribute("wrong_message", "Вы дали неверный ответ. Попробуйте еще раз.");
			}
		}

		forward(RIDDLE_ANSWER_JSP);
	}

	/**
	 * Создание новой загадки
	 */
	private void doCreateRiddle()
	{
		// если указан метод post, то пользователь пытается создать новую загадку
		if ("post".equalsIgnoreCase(req.getMethod())) {
			String title = req.getParameter("title");
			String text = req.getParameter("text");
			String answer_text = req.getParameter("answer");

			int result;
			try {
				result = riddleService.createRiddle(authUser.getId(), title, text, answer_text);
			} catch (DaoException | DbConnectException e) {
				log.error("Сорвалось создание загадки:\n" + e.getMessage(), e);
				forwardError(e);
				return;
			}

			if (result == 0) {
				// создалась новая загадка, переход в список
				req.setAttribute("success_message",
						String.format("Вы создали новую загадку `%s`", title));
				redirect(path + WORKSHOP);
				return;
			}

			// не удалось создать загадку
			// передам заполненные поля
			req.setAttribute("title", title);
			req.setAttribute("text", text);
//			req.setAttribute("image", image);
			req.setAttribute("answer_text", answer_text);

			req.setAttribute("error_message", Messages.get(result));
		}

		forward(RIDDLE_CREATE_JSP);
	}

	/**
	 * Редактирование существующей загадки
	 *
	 * @param uuid_text Номер загадки, берется из запроса
	 */
	private void doEditRiddle(String uuid_text)
	{
		// получаю загадку
		Riddle oldRiddle = _getRiddle(uuid_text);
		if (oldRiddle == null) return;

		// проверка доступа к загадке
		if (!oldRiddle.getUserId().equals(authUser.getId())) {
			req.setAttribute("error_message", "У вас нет прав на редактирование выбранной загадки.");
			forward(WORKSHOP);
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (oldRiddle.getAnswerCount() > 0) {
			req.setAttribute("error_message", "Загадка уже была решена. Ее нельзя редактировать");
			forward(WORKSHOP);
		}

		// поля загадки
		req.setAttribute("riddle_id", oldRiddle.getId().toString());
		req.setAttribute("title", oldRiddle.getTitle());
		req.setAttribute("text", oldRiddle.getText());
		req.setAttribute("image", oldRiddle.getImage());
		req.setAttribute("answer_text", String.join(",", oldRiddle.getAnswers()));

		// если указан метод POST, то пользователь пытается сохранить загадку
		if ("post".equalsIgnoreCase(req.getMethod())) {
			String title = req.getParameter("title");
			String text = req.getParameter("text");
			String answer_text = req.getParameter("answer");

			// пробую обновить загадку
			int result;
			try {
				result = riddleService.updateRiddle(oldRiddle, title, text, answer_text);
			} catch (DaoException | DbConnectException e) {
				log.error(e.getMessage(), e);
				forwardError(e);
				return;
			}

			if (result == 0) {
				// создалась новая загадка, переход в список
				req.setAttribute("success_message",
						String.format("Загадка `%s` сохранена", title));
				redirect(path + WORKSHOP);
				return;
			}

			// не удалось сохранить загадку
			// передам заполненные поля
			req.setAttribute("title", title);
			req.setAttribute("text", text);
			req.setAttribute("answer_text", answer_text);

			req.setAttribute("error_message", Messages.get(result));
		}

		forward(RIDDLE_EDIT_JSP);
	}

	private void doDeleteRiddle(String uuid_text)
	{
		// получаю загадку
		Riddle riddle = _getRiddle(uuid_text);
		if (riddle == null) return;

		// проверка доступа к загадке
		if (!riddle.getUserId().equals(authUser.getId())) {
			req.setAttribute("error_message", "У вас нет прав на редактирование выбранной загадки.");
			forward(WORKSHOP);
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (riddle.getAnswerCount() > 0) {
			req.setAttribute("error_message", "Загадка уже была решена. Ее нельзя редактировать");
			forward(WORKSHOP);
		}

		// проверки прошли, пробую удалить загадку
		int result;
		try {
			result = riddleService.deleteRiddle(riddle);
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			forwardError(e);
			return;
		}

		if (result != 0) {
			// создалась новая загадка, переход в список
			req.setAttribute("error_message",
					String.format("Загадку `%s` не удалось удалить", riddle.getTitle()));
		} else {
			req.setAttribute("success_message",
					String.format("Загадка `%s` удалена", riddle.getTitle()));
		}

		forward(WORKSHOP);
	}
}
