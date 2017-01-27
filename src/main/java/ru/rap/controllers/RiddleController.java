package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.entities.RiddleEntity;
import ru.rap.libraries.PagerLibrary;
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
	//  Controller methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@RequestMapping
	public String getList()
	{
		return doViewRiddles();
	}

	@RequestMapping("answer/{id}")
	public String getAnswer(@PathVariable int id) throws Exception
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
	public String getEdit(@RequestParam int id) throws Exception
	{
		return doEditRiddle(id);
	}

	@RequestMapping("delete/{id}")
	public String getDelete(@RequestParam int id) throws Exception
	{
		return doDeleteRiddle(id);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Common
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Возвращает загадку по номеру
	 *
	 * @return null или загадка
	 */
	private RiddleEntity _getRiddle(int id) throws Exception
	{
		return riddleService.findOne(id);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Выводит список загадок
	 */
	private String doViewRiddles()
	{
		int count;

		Integer authUserId = getPrincipalDetails().getUser().getId();

		try {
			// сколько всего загадок может видеть текущий пользователь
			// это нужно для того, чтобы обрезать выборку
			count = riddleService.getCountNotOfUser(authUserId);
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась выборка количества записей:\n" + e.getMessage(), e);
			return reportAndForwardError(e);
		}

		// данные для пагинатора
		int pageCount = (int) Math.ceil(1. * count / ITEMS_PER_PAGE);
		int pageIndex = PagerLibrary.getRiddlesPageIndex(request);
		pageIndex = Math.max(1, Math.min(pageCount, pageIndex));

		// получаю список загадок не текущего пользователя с учетом пагинатора
		Map<RiddleEntity, Timestamp> riddles;

		riddles = riddleService.getListNotOfUser(authUserId);

		request.setAttribute("pageCount", pageCount);
		request.setAttribute("pageIndex", pageIndex);
		request.setAttribute("riddles", riddles);

		return PAGE_RIDDLES;
	}

	/**
	 * Отгадывание выбранной загадки
	 */
	private String doAnswerRiddle(int id) throws Exception
	{
		// получаю загадку
		RiddleEntity riddle = _getRiddle(id);
		if (riddle == null) {
			request.setAttribute("error_message", "Загадка не существует. Возможно, украли инопланетяне.");
			return PAGE_RIDDLES;
		}

		// проверка доступа к загадке
		if (riddle.getRiddler().getId()
				.equals(getPrincipalDetails().getUser().getId())) {
			request.setAttribute("error_message", "Вы не можете отгадывать свои загадки.");
			return PAGE_RIDDLES;
		}

		// название загадки
		request.setAttribute("title", riddle.getTitle());

		// если загадка уже была решена этим пользователей, то ее нельзя решать снова
		try {
			if (answerService.isAnswerRight(
					getPrincipalDetails().getUser().getId(),
					riddle.getId())) {
				request.setAttribute("error_message", "Загадка уже была отгадана вами. Ее нельзя отгадывать снова.");
				return PAGE_RIDDLE_ANSWER;
			}
		} catch (DaoException | DbConnectException e) {
			log.error("Сорвалась проверка решеной загадки:\n" + e.getMessage(), e);
			return reportAndForwardError(e);
		}

		// поля загадки для frontend
		request.setAttribute("text", riddle.getText());
		request.setAttribute("image", riddle.getImage());

		// если указан метод post, то пользователь пытается решить загадку
		if ("post".equalsIgnoreCase(request.getMethod())) {
			String answer = request.getParameter("answer")
					.trim().toLowerCase().replace("[\\s]{2,}", " ");

			riddleService.answer(getPrincipalDetails().getUser(), riddle, answer);
//			} catch (DbConnectException | DaoException e) {
//				log.error("Сорвалась проверка отгадки:\n" + e.getMessage(), e);
//				return reportAndForwardError(e);
//			}

//			if (result == 1) {
//				request.setAttribute("success_messages", "Вы отгадали загадку");
//				return PAGE_RIDDLES;
//			} else {
//				request.setAttribute("wrong_message", "Вы дали неверный ответ. Попробуйте еще раз.");
//			}
		}

		return PAGE_RIDDLE_ANSWER;
	}

	private String doViewMyRiddles()
	{
		// получаю количество загадок не текущего пользователя
		int count = riddleService.getCountOfUser(getPrincipalDetails().getUser().getId());

		// какой номер страницы
		int pageIndex = PagerLibrary.getWorkshopPageIndex(request);
		int pageCount = (int) Math.ceil(1. * count / ITEMS_PER_PAGE);
		pageIndex = Math.max(1, Math.min(pageCount, pageIndex));

		// получаю список загадок не текущего пользователя
		List<RiddleEntity> riddles = riddleService.getListOfUser(getPrincipalDetails().getUser().getId());

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

			int result = riddleService.createRiddle(getPrincipalDetails().getUser(), title, text, answer_text);

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
	 */
	private String doEditRiddle(int id) throws Exception
	{
		// получаю загадку
		RiddleEntity oldRiddle = _getRiddle(id);
		if (oldRiddle == null) return redirectTo(PAGE_RIDDLE_MINE);

		// проверка доступа к загадке
		if (!oldRiddle.getRiddler().getId()
				.equals(getPrincipalDetails().getUser().getId())) {
			request.setAttribute("error_message", "У вас нет прав на редактирование выбранной загадки.");
			return PAGE_RIDDLE_MINE;
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (oldRiddle.getAnsweredCount() > 0) {
			request.setAttribute("error_message", "Загадка уже была решена. Ее нельзя редактировать");
			return PAGE_RIDDLE_MINE;
		}

		// поля загадки
		request.setAttribute("riddleId", oldRiddle.getId().toString());
		request.setAttribute("title", oldRiddle.getTitle());
		request.setAttribute("text", oldRiddle.getText());
		request.setAttribute("image", oldRiddle.getImage());
		request.setAttribute("answer_text", oldRiddle.getAnswerData());

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

	private String doDeleteRiddle(int id) throws Exception
	{
		// получаю загадку
		RiddleEntity riddle = _getRiddle(id);
		if (riddle == null)
			return PAGE_RIDDLE_MINE;

		// проверка доступа к загадке
		if (!riddle.getRiddler().getId()
				.equals(getPrincipalDetails().getUser().getId())) {
			request.setAttribute("error_message", "У вас нет прав на редактирование выбранной загадки.");
			return PAGE_RIDDLE_MINE;
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (riddle.getAnsweredCount() > 0) {
			request.setAttribute("error_message", "Загадка уже была решена. Ее нельзя редактировать");
			return PAGE_RIDDLE_MINE;
		}

		// проверки прошли, пробую удалить загадку
		riddleService.deleteRiddle(riddle);

//		if (result != 0) {
//			// создалась новая загадка, переход в список
//			request.setAttribute("error_message",
//					String.format("Загадку `%s` не удалось удалить", riddle.getTitle()));
//		} else {
//			request.setAttribute("success_message",
//					String.format("Загадка `%s` удалена", riddle.getTitle()));
//		}

		return PAGE_RIDDLE_MINE;
	}
}
