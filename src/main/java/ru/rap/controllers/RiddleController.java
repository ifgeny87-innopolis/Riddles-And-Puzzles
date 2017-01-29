package ru.rap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.rap.common.Messages;
import ru.rap.models.RiddleModel;
import ru.rap.services.AnswerService;
import ru.rap.services.RiddleService;
import ru.rap.services.UserService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private RiddleService riddleService;

	private UserService userService;

	private AnswerService answerService;

	@Autowired
	public void setRiddleService(RiddleService riddleService)
	{
		this.riddleService = riddleService;
	}

	@Autowired
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	@Autowired
	public void setAnswerService(AnswerService answerService)
	{
		this.answerService = answerService;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Controller methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@RequestMapping
	public ModelAndView getList()
	{
		return doViewRiddles();
	}

	@RequestMapping("answer/{id}")
	public ModelAndView getAnswer(@PathVariable int id)
	{
		return doAnswerRiddle(id);
	}

	@RequestMapping("my")
	public ModelAndView getMyList()
	{
		return doViewMyRiddles();
	}

	@RequestMapping("create")
	public ModelAndView getCreate()
	{
		return doCreateRiddle();
	}

	@RequestMapping("edit/{id}")
	public ModelAndView getEdit(@PathVariable("id") int id)
	{
		return doEditRiddle(id);
	}

	@RequestMapping("delete/{id}")
	public ModelAndView getDelete(@PathVariable("id") int id)
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
	private RiddleModel _getRiddle(int id)
	{
		return riddleService.findOne(id);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Выводит список загадок
	 */
	private ModelAndView doViewRiddles()
	{
		Integer authUserId = getPrincipalDetails().getUser().getId();

		// получаю список загадок не текущего пользователя с учетом пагинатора
		Map<RiddleModel, Timestamp> riddles = riddleService.getListNotOfUser(authUserId);

		return new ModelAndView(PAGE_RIDDLES, "riddles", riddles);
	}

	/**
	 * Отгадывание выбранной загадки
	 */
	private ModelAndView doAnswerRiddle(int id)
	{
		// получаю загадку
		RiddleModel riddle = _getRiddle(id);
		if (riddle == null) {
			return new ModelAndView(PAGE_RIDDLES,
					"error_message",
					"Загадка не существует. Возможно, украли инопланетяне.");
		}

		// проверка доступа к загадке
		Integer principalId = getPrincipalDetails().getUser().getId();

		if (riddle
				.getRiddlerId()
				.equals(principalId)) {
			return new ModelAndView(PAGE_RIDDLES,
					"error_message",
					"Вы не можете отгадывать свои загадки.");
		}

		// название загадки
		Map<String, Object> model = new HashMap<>();
		model.put("title", riddle.getTitle());

		// если загадка уже была решена этим пользователей, то ее нельзя решать снова
		if (answerService.isAnswerRight(
				principalId,
				riddle.getId())) {
			model.put("error_message", "Загадка уже была отгадана вами. Ее нельзя отгадывать снова.");
		} else {
			// поля загадки для frontend
			model.put("text", riddle.getText());
			model.put("image", riddle.getImage());

			// если указан метод post, то пользователь пытается решить загадку
			if ("post".equalsIgnoreCase(request.getMethod())) {
				String answer = request.getParameter("answer")
						.trim().toLowerCase().replace("[\\s]{2,}", " ");

				riddleService.answer(
						getPrincipalDetails().getUser().getId(),
						riddle.getId(),
						answer);
			}
		}

		return new ModelAndView(PAGE_RIDDLE_ANSWER, model);
	}

	private ModelAndView doViewMyRiddles()
	{
		Integer principalId = getPrincipalDetails().getUser().getId();

		// получаю список загадок не текущего пользователя
		List<RiddleModel> riddles = riddleService.getListOfUser(principalId);

		return new ModelAndView(PAGE_RIDDLE_MINE, "riddles", riddles);
	}

	/**
	 * Создание новой загадки
	 */
	private ModelAndView doCreateRiddle()
	{
		Map<String, Object> model = new HashMap<>();

		Integer principalId = getPrincipalDetails().getUser().getId();

		// если указан метод post, то пользователь пытается создать новую загадку
		if ("post".equalsIgnoreCase(request.getMethod())) {
			String title = request.getParameter("title");
			String text = request.getParameter("text");
			String answer_text = request.getParameter("answer");

			int result = riddleService.createRiddle(principalId, title, text, answer_text);

			if (result == 0) {
				// создалась новая загадка, переход в список
				return new ModelAndView(PAGE_RIDDLE_MINE,
						"success_message",
						String.format("Вы создали новую загадку `%s`", title));
			}

			// не удалось создать загадку
			// передам заполненные поля
			model.put("title", title);
			model.put("text", text);
//			req.setAttribute("image", image);
			model.put("answer_text", answer_text);

			model.put("error_message", Messages.get(result));
		}

		return new ModelAndView(PAGE_RIDDLE_CREATE, model);
	}

	/**
	 * Редактирование существующей загадки
	 */
	private ModelAndView doEditRiddle(int id)
	{
		// получаю загадку
		RiddleModel oldRiddle = _getRiddle(id);
		if (oldRiddle == null) {
			return new ModelAndView(redirectTo(PAGE_RIDDLE_MINE));
		}

		// проверка доступа к загадке
		if (!oldRiddle.getRiddlerId()
				.equals(getPrincipalDetails().getUser().getId())) {
			return new ModelAndView(PAGE_RIDDLE_MINE,
					"error_message",
					"У вас нет прав на редактирование выбранной загадки.");
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (oldRiddle.getAnsweredCount() > 0) {
			return new ModelAndView(PAGE_RIDDLE_MINE,
					"error_message",
					"Загадка уже была решена. Ее нельзя редактировать");
		}

		// поля загадки
		Map<String, Object> model=new HashMap<>();
		model.put("riddleId", oldRiddle.getId().toString());
		model.put("title", oldRiddle.getTitle());
		model.put("text", oldRiddle.getText());
		model.put("image", oldRiddle.getImage());
		model.put("answer_text", oldRiddle.getAnswers());

		// если указан метод POST, то пользователь пытается сохранить загадку
		if ("post".equalsIgnoreCase(request.getMethod())) {
			String title = request.getParameter("title");
			String text = request.getParameter("text");
			String answer_text = request.getParameter("answer");

			// пробую обновить загадку
			int result= riddleService.updateRiddle(oldRiddle, title, text, answer_text);

			if (result == 0) {
				// создалась новая загадка, переход в список
				model.put("success_message",
						String.format("Загадка `%s` сохранена", title));
				return new ModelAndView(PAGE_RIDDLE_MINE, model);
			}

			// не удалось сохранить загадку
			// передам заполненные поля
			model.put("title", title);
			model.put("text", text);
			model.put("answer_text", answer_text);

			model.put("error_message", Messages.get(result));
		}

		return new ModelAndView(PAGE_RIDDLE_EDIT, model);
	}

	private ModelAndView doDeleteRiddle(int id)
	{
		// получаю загадку
		RiddleModel riddle = _getRiddle(id);
		if (riddle == null) {
			return new ModelAndView(PAGE_RIDDLE_MINE);
		}

		// проверка доступа к загадке
		if (!riddle.getRiddlerId()
				.equals(getPrincipalDetails().getUser().getId())) {
			return new ModelAndView(PAGE_RIDDLE_MINE,
					"error_message",
					"У вас нет прав на редактирование выбранной загадки.");
		}

		// если загадка уже была решена, то ее нельзя редактировать
		if (riddle.getAnsweredCount() > 0) {
			return new ModelAndView(PAGE_RIDDLE_MINE,
					"error_message",
					"Загадка уже была решена. Ее нельзя редактировать");
		}

		// проверки прошли, пробую удалить загадку
		riddleService.deleteRiddle(riddle.getId());

//		if (result != 0) {
//			// создалась новая загадка, переход в список
//			request.setAttribute("error_message",
//					String.format("Загадку `%s` не удалось удалить", riddle.getTitle()));
//		} else {
//			request.setAttribute("success_message",
//					String.format("Загадка `%s` удалена", riddle.getTitle()));
//		}

		return new ModelAndView(PAGE_RIDDLE_MINE);
	}
}
