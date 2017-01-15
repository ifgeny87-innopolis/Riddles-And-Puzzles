package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.libraries.PagerLibrary;
import ru.rap.models.Riddle;
import ru.rap.services.AnswerService;
import ru.rap.services.RiddleService;
import ru.rap.services.UserService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.rap.common.Config.ITEMS_PER_PAGE;
import static ru.rap.common.PageList.*;

/**
 * Сервлет-контроллер управления загадками
 *
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
@Controller
@RequestMapping(PAGE_RIDDLES)
public class RiddleController extends BaseController
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RiddleController.class);

	@Autowired
	private RiddleService riddleService;

	@Autowired
	private UserService userService;

	@Autowired
	private AnswerService answerService;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  CONTROLLER METHODS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@RequestMapping
	public String getList()
	{
		return doViewRiddles();
	}

	@RequestMapping("answer/{id}")
	public String getAnswer(@PathVariable String id) throws Exception
	{
		return doAnswerRiddle(id);
	}

	@RequestMapping("my")
	public String getMyList()
	{
		return doViewMyRiddles();
	}

	@RequestMapping("create")
	public String getCreate()
	{
		return doCreateRiddle();
	}

	@RequestMapping("edit/{id}")
	public String getEdit(@RequestParam String id) throws Exception
	{
		return doEditRiddle(id);
	}

	@RequestMapping("delete/{id}")
	public String getDelete(@RequestParam String id) throws Exception
	{
		return doDeleteRiddle(id);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  COMMON
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Возвращает загадку по номеру
	 *
	 * @param uuid_text Номер загадки в тексте
	 * @return null или загадка
	 */
	private Riddle _getRiddle(String uuid_text) throws Exception
	{
		try {
			return riddleService.getRiddle(UUID.fromString(uuid_text));
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась выборка загадки:\n" + e.getMessage(), e);
			reportAndForwardError(e);
			throw new Exception();
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  ACTIONS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Выводит список загадок
	 */
	private String doViewRiddles()
	{
		int count;
		try {
			// получаю количество загадок не текущего пользователя
			count = riddleService.getCountNotOfUser(getPrincipal().getUser().getId());
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась выборка количества записей:\n" + e.getMessage(), e);
			return reportAndForwardError(e);
		}

		// какой номер страницы
		int pageIndex = PagerLibrary.getRiddlesPageIndex(request);
		int pageCount = (int) Math.ceil(1. * count / ITEMS_PER_PAGE);
		pageIndex = Math.max(1, Math.min(pageCount, pageIndex));

		// получаю список загадок не текущего пользователя
		Map<Riddle, Timestamp> riddles;
		try {
			riddles = riddleService.getListNotOfUser(getPrincipal().getUser().getId(),
					(pageIndex - 1) * ITEMS_PER_PAGE,
					ITEMS_PER_PAGE);
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалось получение списка:\n" + e.getMessage(), e);
			return reportAndForwardError(e);
		}

		request.setAttribute("pageCount", pageCount);
		request.setAttribute("pageIndex", pageIndex);
		request.setAttribute("riddles", riddles);

		return PAGE_RIDDLES;
	}

	/**
	 * Отгадывание выбранной загадки
	 */
	private String doAnswerRiddle(String uuid_text) throws Exception
	{
		// получаю загадку
		Riddle riddle = _getRiddle(uuid_text);
		if (riddle == null) {
			request.setAttribute("error_message", "Загадка не существует. Возможно, украли инопланетяне.");
			return PAGE_RIDDLES;
		}

		// проверка доступа к загадке
		if (riddle.getUserId().equals(getPrincipal().getUser().getId())) {
			request.setAttribute("error_message", "Вы не можете отгадывать свои загадки.");
			return PAGE_RIDDLES;
		}

		// если загадка уже была решена этим пользователей, то ее нельзя решать снова
		try {
			if (answerService.isAnswerRight(getPrincipal().getUser().getId(), riddle.getId())) {
				request.setAttribute("error_message", "Загадка уже была отгадана вами. Ее нельзя отгадывать снова.");
				return PAGE_RIDDLES;
			}
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась проверка решеной загадки:\n" + e.getMessage(), e);
			return reportAndForwardError(e);
		}

		// поля загадки для frontend
		request.setAttribute("title", riddle.getTitle());
		request.setAttribute("text", riddle.getText());
		request.setAttribute("image", riddle.getImage());

		// если указан метод post, то пользователь пытается решить загадку
		if ("post".equalsIgnoreCase(request.getMethod())) {
			String answer = request.getParameter("answer")
					.trim().toLowerCase().replace("[\\s]{2,}", " ");

			int result;
			try {
				result = riddleService.answer(getPrincipal().getUser(), riddle, answer);
			} catch (DbConnectException | DaoException e) {
				log.error("Сорвалась проверка отгадки:\n" + e.getMessage(), e);
				return reportAndForwardError(e);
			}

			if (result == 1) {
				request.setAttribute("success_messages", "Вы отгадали загадку");
				return PAGE_RIDDLES;
			} else {
				request.setAttribute("wrong_message", "Вы дали неверный ответ. Попробуйте еще раз.");
			}
		}

		return PAGE_RIDDLE_ANSWER;
	}

	private String doViewMyRiddles()
	{
		int count;
		try {
			// получаю количество загадок не текущего пользователя
			count = riddleService.getCountOfUser(getPrincipal().getUser().getId());
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалось получение количества:\n" + e.getMessage(), e);
			return reportAndForwardError(e);
		}

		// какой номер страницы
		int pageIndex = PagerLibrary.getWorkshopPageIndex(request);
		int pageCount = (int) Math.ceil(1. * count / ITEMS_PER_PAGE);
		pageIndex = Math.max(1, Math.min(pageCount, pageIndex));

		// получаю список загадок не текущего пользователя
		List<Riddle> riddles;
		try {
			riddles = riddleService.getListOfUser(getPrincipal().getUser().getId(),
					(pageIndex - 1) * ITEMS_PER_PAGE,
					ITEMS_PER_PAGE);
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалось получение загадок:\n" + e.getMessage(), e);
			return reportAndForwardError(e);
		}

		request.setAttribute("pageCount", pageCount);
		request.setAttribute("pageIndex", pageIndex);
		request.setAttribute("riddles", riddles);

		return PAGE_RIDDLE_MINE;
	}

	/**
	 * Создание новой загадки
	 */
	private String doCreateRiddle()
	{
		// если указан метод post, то пользователь пытается создать новую загадку
		if ("post".equalsIgnoreCase(request.getMethod())) {
			String title = request.getParameter("title");
			String text = request.getParameter("text");
			String answer_text = request.getParameter("answer");

			int result;
			try {
				result = riddleService.createRiddle(getPrincipal().getUser().getId(), title, text, answer_text);
			} catch (DaoException | DbConnectException e) {
				log.error("Сорвалось создание загадки:\n" + e.getMessage(), e);
				return reportAndForwardError(e);
			}

			if (result == 0) {
				// создалась новая загадка, переход в список
				request.setAttribute("success_message",
						String.format("Вы создали новую загадку `%s`", title));
				return PAGE_RIDDLE_MINE;
			}

			// не удалось создать загадку
			// передам заполненные поля
			request.setAttribute("title", title);
			request.setAttribute("text", text);
//			req.setAttribute("image", image);
			request.setAttribute("answer_text", answer_text);

			request.setAttribute("error_message", Messages.get(result));
		}

		return PAGE_RIDDLE_CREATE;
	}

	/**
	 * Редактирование существующей загадки
	 *
	 * @param uuid_text Номер загадки, берется из запроса
	 */
	private String doEditRiddle(String uuid_text) throws Exception
	{
		// получаю загадку
		Riddle oldRiddle = _getRiddle(uuid_text);
		if (oldRiddle == null) return redirectTo(PAGE_RIDDLE_MINE);

		// проверка доступа к загадке
		if (!oldRiddle.getUserId().equals(getPrincipal().getUser().getId())) {
			request.setAttribute("error_message", "У вас нет прав на редактирование выбранной загадки.");
			return PAGE_RIDDLE_MINE;
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (oldRiddle.getAnswerCount() > 0) {
			request.setAttribute("error_message", "Загадка уже была решена. Ее нельзя редактировать");
			return PAGE_RIDDLE_MINE;
		}

		// поля загадки
		request.setAttribute("riddle_id", oldRiddle.getId().toString());
		request.setAttribute("title", oldRiddle.getTitle());
		request.setAttribute("text", oldRiddle.getText());
		request.setAttribute("image", oldRiddle.getImage());
		request.setAttribute("answer_text", String.join(",", oldRiddle.getAnswers()));

		// если указан метод POST, то пользователь пытается сохранить загадку
		if ("post".equalsIgnoreCase(request.getMethod())) {
			String title = request.getParameter("title");
			String text = request.getParameter("text");
			String answer_text = request.getParameter("answer");

			// пробую обновить загадку
			int result;
			try {
				result = riddleService.updateRiddle(oldRiddle, title, text, answer_text);
			} catch (DaoException | DbConnectException e) {
				log.error(e.getMessage(), e);
				return reportAndForwardError(e);
			}

			if (result == 0) {
				// создалась новая загадка, переход в список
				request.setAttribute("success_message",
						String.format("Загадка `%s` сохранена", title));
				return PAGE_RIDDLE_MINE;
			}

			// не удалось сохранить загадку
			// передам заполненные поля
			request.setAttribute("title", title);
			request.setAttribute("text", text);
			request.setAttribute("answer_text", answer_text);

			request.setAttribute("error_message", Messages.get(result));
		}

		return PAGE_RIDDLE_EDIT;
	}

	private String doDeleteRiddle(String uuid_text) throws Exception
	{
		// получаю загадку
		Riddle riddle = _getRiddle(uuid_text);
		if (riddle == null)
			return PAGE_RIDDLE_MINE;

		// проверка доступа к загадке
		if (!riddle.getUserId().equals(getPrincipal().getUser().getId())) {
			request.setAttribute("error_message", "У вас нет прав на редактирование выбранной загадки.");
			return PAGE_RIDDLE_MINE;
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (riddle.getAnswerCount() > 0) {
			request.setAttribute("error_message", "Загадка уже была решена. Ее нельзя редактировать");
			return PAGE_RIDDLE_MINE;
		}

		// проверки прошли, пробую удалить загадку
		int result;
		try {
			result = riddleService.deleteRiddle(riddle);
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			return reportAndForwardError(e);
		}

		if (result != 0) {
			// создалась новая загадка, переход в список
			request.setAttribute("error_message",
					String.format("Загадку `%s` не удалось удалить", riddle.getTitle()));
		} else {
			request.setAttribute("success_message",
					String.format("Загадка `%s` удалена", riddle.getTitle()));
		}

		return PAGE_RIDDLE_MINE;
	}
}
