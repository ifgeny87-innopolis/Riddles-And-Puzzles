package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiServiceExporter;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.entities.AnswerEntity;
import ru.rap.entities.RiddleEntity;
import ru.rap.entities.UserEntity;
import ru.rap.models.RiddleModel;
import ru.rap.repositories.RiddleRepository;
import ru.rap.validators.StrValidator;

import java.sql.Timestamp;
import java.util.*;

/**
 * Сервис работы с загадками
 *
 * Created in project RiddlesAndPuzzles in 27.12.2016
 */
public class RiddleService extends BaseService<RiddleModel>
{
	// Logger
	private static final Logger log = LoggerFactory.getLogger(RiddleService.class);

	@Autowired
	private RiddleRepository riddleRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AnswerService answerService;

	/**
	 * Вернет количество загадок, НЕ созданных указанным пользователем
	 *
	 * @param userId Номер пользователя
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public int getCountNotOfUser(int userId) throws DaoException, DbConnectException
	{
		return riddleRepository.countNotByUserId(userId);
	}

	/**
	 * Вернет список загадок, НЕ созданных указанным пользователем
	 *
	 * @param userId Номер пользователя
	 * @return
	 */
	public Map<RiddleEntity, Timestamp> getListNotOfUser(int userId)
	{
		List<RiddleEntity> _riddles = riddleRepository.findNotByUserId(userId);

		if (_riddles == null)
			return null;

		// для каждой загадки определяю:
		// - отдагал ли ее указанный пользователь
		// - когда он ее отгадал
		int[] _ids = _riddles
				.stream()
				.mapToInt(RiddleEntity::getId)
				.toArray();

		Map<Integer, AnswerEntity> answers = answerService.getFor(userId, _ids);

		// список отгадок получен, разбираю
		Map<RiddleEntity, Timestamp> result = new HashMap<>();

		for (RiddleEntity riddle : _riddles) {
			Integer _riddleId = riddle.getId();
			if (answers.containsKey(_riddleId)) {
				AnswerEntity _answer = answers.get(_riddleId);
				Timestamp _timestamp = _answer.getCreated();
				result.put(riddle, _timestamp);
			} else {
				result.put(riddle, null);
			}
		}

		return result;
	}

	/**
	 * Вернет количество загадок, созданных указанным пользователем
	 */
	public int getCountOfUser(int userId)
	{
		return riddleRepository.countByUserId(userId);
	}

	/**
	 * Вернет список загадок, созданных указанным пользователем
	 */
	public List<RiddleEntity> getListOfUser(int userId)
	{
		return riddleRepository.findByUserId(userId);
	}

	/**
	 * Выполняет валидацию данных
	 * Может вернуть число, в случае ошибки проверки
	 * Может вернуть массив с измененными полями, вот такой:
	 * { String title, String text, String[] answers }
	 *
	 * @param title       Название
	 * @param text        Текст
	 * @param answer_text Строка, содержащая один или несколько ответов
	 * @return Номер ошибки или массив с данными
	 */
	private Object validate(String title, String text, String answer_text)
	{
		title = title.trim();
		text = text.trim();
		answer_text = answer_text.trim();

		if (!StrValidator.validateLength(title, 5, 100)) {
			return Messages.WRONG_RIDDLE_TITLE_LENGTH;
		} else if (!StrValidator.validateLength(text, 5, 1000)) {
			return Messages.WRONG_RIDDLE_TEXT_LENGTH;
		} else if (!StrValidator.validateLength(answer_text, 1, 100)) {
			return Messages.WRONG_RIDDLE_ANSWER_LENGTH;
		}

		// проверяю список ответов, удаляю пустые
		int prev = -1;
		String[] answers = answer_text.split(",");
		for (int i = 0; i < answers.length; i++) {
			String s = answers[i].replaceAll("[\\s]{2,}", " ")
					.trim().toLowerCase();
			if (!s.isEmpty())
				answers[++prev] = s;
		}

		// если изменилось количество, меняю массив
		if (prev + 1 != answers.length)
			answers = Arrays.copyOf(answers, prev + 1);

		// еще раз проверяю ответы
		if (prev < 0) {
			return Messages.WRONG_RIDDLE_ANSWER_LENGTH;
		}

		return new Object[]{title, text, answers};
	}

	/**
	 * Создание новой загадки
	 */
	public int createRiddle(UserEntity riddler, String title, String text, String answerData)
	{
		// сначала валидация полей
		Object res = validate(title, text, answerData);

		if (res instanceof Integer) return (Integer) res;

		// получаю результат
		Object[] list = (Object[]) res;
		title = (String) list[0];
		text = (String) list[1];
		answerData = (String) list[2];

		riddleRepository.save(
				new RiddleEntity()
						.setRiddler(riddler)
						.setTitle(title)
						.setText(text)
						.setAnswerData(answerData)
		);

		return 0;
	}

	/**
	 * Выборка загадки по номеру
	 *
	 * @param id Номер загадки
	 * @return null или объект загадки
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public RiddleEntity findOne(int id) throws DbConnectException, DaoException
	{
		return riddleRepository.findOne(id);
	}

	/**
	 * Обновление существующей загадки
	 *
	 * @param oldRiddle   Объект редактируемой зашадки
	 * @param title       Новое название
	 * @param text        Новый текст загадки
	 * @param answer_text Новые ответы
	 * @return 0 или номер ошибки
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public int updateRiddle(RiddleEntity oldRiddle, String title, String text, String answer_text) throws DbConnectException, DaoException
	{
		// сначала валидация полей
		Object res = validate(title, text, answer_text);

		if (res instanceof Integer) return (Integer) res;

		// получаю результат
		Object[] list = (Object[]) res;
		title = (String) list[0];
		text = (String) list[1];
		String[] answers = (String[]) list[2];

//		boolean ok;
//		try {
//			ok = riddleRepository.update(new RiddleModel(oldRiddle.getId(), oldRiddle.getUserId(), title, text, oldRiddle.getImage(), answers, oldRiddle.getAnsweredCount(), oldRiddle.getAttemptCount(), oldRiddle.getCreated(), null));
//			riddleRepository.commit();
//		} catch (DaoException | DbConnectException e) {
//			log.error(e.getMessage(), e);
//			// делаю откат
//			riddleRepository.rollback();
//			throw e;
//		}

		return 0;
	}

	/**
	 * Удаление загадки
	 *
	 * @param riddle
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public void deleteRiddle(RiddleEntity riddle)
	{
		riddleRepository.delete(riddle);
	}

	/**
	 * Выполнение ответа на загадку
	 *
	 * @param user
	 * @param riddle
	 * @param answer
	 * @return
	 */
	public void answer(UserEntity user, RiddleEntity riddle, String answer) throws DbConnectException, DaoException
	{
		// есть ли ответ среди правильных?
		boolean is_right = riddle.getAnswerData().equals(answer);

		// добавляю отгадку
		answerService.insert(user, riddle, answer, is_right);

		// увеличиваю счетчик попыток и отгадок для самой загадки и пользователя
//			if (incTries(riddle, is_right) == 0
//					&& userService.incTries(user, is_right) == 0) {
//				riddleRepository.commit();
//			} else {
//				throw new DaoException("Не удалось обновить счетчики");
//			}
	}

//	/**
//	 * Увеличивает счетчик попыток
//	 * Если указан флаг rightsToo, то счетчик верных ответов тоже будет увеличен
//	 *
//	 * @param r         Загадка
//	 * @param rightsToo Нужно ли также увеличить счетчик верных ответов
//	 * @return 0 или номер ошибки
//	 */
//	private int incTries(RiddleModel r, boolean rightsToo) throws DbConnectException, DaoException
//	{
//		int answer_count = r.getAnsweredCount() + (rightsToo ? 1 : 0);
//		int try_count = r.getAttemptCount() + 1;
//
//		return riddleRepository.update(new RiddleModel(r.getId(), r.getUserId(), r.getTitle(), r.getText(), r.getImage(), r.getAnswers(), answer_count, try_count, r.getCreated(), null)) ? 0 : -1;
//	}
}