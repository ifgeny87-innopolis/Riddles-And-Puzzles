package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rap.common.Messages;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.dao.RiddleDao;
import ru.rap.models.Answer;
import ru.rap.models.Riddle;
import ru.rap.models.User;
import ru.rap.validators.StrValidator;

import java.sql.Timestamp;
import java.util.*;

/**
 * Сервис работы с загадками
 *
 * Created in project RiddlesAndPuzzles in 27.12.2016
 */
public class RiddleService extends BaseService<Riddle>
{
	// Logger
	private static final Logger log = LoggerFactory.getLogger(RiddleService.class);

	@Override
	protected Logger getLogger() { return log;}

	@Autowired
	private RiddleDao dao;

	@Autowired
	private UserService userService;

	@Override
	protected RiddleDao getDao() { return dao;}

	/**
	 * Вернет количество загадок, НЕ созданных указанным пользователем
	 *
	 * @param userId Номер пользователя
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public int getCountNotOfUser(UUID userId) throws DaoException, DbConnectException
	{
		return dao.count("WHERE user_id<>?", userId);
	}

	/**
	 * Вернет список загадок, НЕ созданных указанным пользователем
	 *
	 * @param userId Номер пользователя
	 * @param start  Начало
	 * @param count  Конец
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public Map<Riddle, Timestamp> getListNotOfUser(UUID userId, int start, int count) throws DaoException, DbConnectException
	{
		List<Riddle> r_ = dao.select("WHERE user_id<>? LIMIT ?,?", userId, start, count);
		if(r_ == null)
			return null;

		// теперь для каждой загадки надо получить время и статус отгадки
		UUID[] u_ = r_.stream().map(r -> r.getId()).toArray(UUID[]::new);
		Map<UUID, Answer> answers = AnswerService.getInstance().getFor(userId, u_);

		// список отгадок получен, разбираю
		Map<Riddle, Timestamp> result = new HashMap<>();
		for (Riddle r : r_) {
			UUID rid = r.getId();
			if(answers.containsKey(rid)) {
				Answer a = answers.get(rid);
				Timestamp t = a.getCreated();
				result.put(r, t);
			}
			else {
				result.put(r,null);
			}
		}

		return result;
	}

	/**
	 * Вернет количество загадок, созданных указанным пользователем
	 *
	 * @param userId Номер пользователя
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public int getCountOfUser(UUID userId) throws DaoException, DbConnectException
	{
		return dao.count("WHERE user_id=?", userId);
	}

	/**
	 * Вернет список загадок, созданных указанным пользователем
	 *
	 * @param userId Номер пользователя
	 * @param start  Начало
	 * @param count  Конец
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public List<Riddle> getListOfUser(UUID userId, int start, int count) throws DaoException, DbConnectException
	{
		return dao.select("WHERE user_id=? LIMIT ?,?", userId, start, count);
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
	 *
	 * @param userId      Uuid пользователя
	 * @param title       Название загадки
	 * @param text        Текст загадки
	 * @param answer_text Список правильных ответов через запятую
	 * @return 0 или номер ошибки
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public int createRiddle(UUID userId, String title, String text, String answer_text) throws DbConnectException, DaoException
	{
		// сначала валидация полей
		Object res = validate(title, text, answer_text);

		if (res instanceof Integer) return (Integer) res;

		// получаю результат
		Object[] list = (Object[]) res;
		title = (String) list[0];
		text = (String) list[1];
		String[] answers = (String[]) list[2];

		boolean ok;
		try {
			ok = dao.insert(new Riddle(userId, title, text, null, answers));
			dao.commit();
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			// делаю откат
			dao.rollback();
			throw e;
		}

		return (ok) ? 0 : -1;
	}

	/**
	 * Выборка загадки по номеру
	 *
	 * @param id Номер загадки
	 * @return null или объект загадки
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public Riddle getRiddle(UUID id) throws DbConnectException, DaoException
	{
		return dao.selectOneBy("id", id);
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
	public int updateRiddle(Riddle oldRiddle, String title, String text, String answer_text) throws DbConnectException, DaoException
	{
		// сначала валидация полей
		Object res = validate(title, text, answer_text);

		if (res instanceof Integer) return (Integer) res;

		// получаю результат
		Object[] list = (Object[]) res;
		title = (String) list[0];
		text = (String) list[1];
		String[] answers = (String[]) list[2];

		boolean ok;
		try {
			ok = dao.update(new Riddle(oldRiddle.getId(), oldRiddle.getUserId(), title, text, oldRiddle.getImage(), answers, oldRiddle.getAnswerCount(), oldRiddle.getTryCount(), oldRiddle.getCreated(), null));
			dao.commit();
		} catch (DaoException | DbConnectException e) {
			log.error(e.getMessage(), e);
			// делаю откат
			dao.rollback();
			throw e;
		}

		return (ok) ? 0 : -1;
	}

	/**
	 * Удаление загадки
	 *
	 * @param riddle
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public int deleteRiddle(Riddle riddle) throws DaoException, DbConnectException
	{
		return dao.delete(riddle) ? 0 : -1;
	}

	/**
	 * Выполнение ответа на загадку
	 *
	 * @param user
	 * @param riddle
	 * @param answer
	 * @return
	 */
	public int answer(User user, Riddle riddle, String answer) throws DbConnectException, DaoException
	{
		// есть ли ответ среди правильных?
		boolean is_right = Arrays.asList(riddle.getAnswers()).contains(answer);

		try {
			// добавляю отгадку
			AnswerService.getInstance().insert(user.getId(), riddle.getId(), answer, is_right);
			// увеличиваю счетчик попыток и отгадок для самой загадки и пользователя
			if (incTries(riddle, is_right) == 0
					&& userService.incTries(user, is_right) == 0) {
				dao.commit();
			} else {
				throw new DaoException("Не удалось обновить счетчики");
			}
		} catch (DaoException | DbConnectException e) {
			dao.rollback();
			log.error(e.getMessage(), e);
			throw e;
		}

		return (is_right) ? 1 : 0;
	}

	/**
	 * Увеличивает счетчик попыток
	 * Если указан флаг rightsToo, то счетчик верных ответов тоже будет увеличен
	 *
	 * @param r         Загадка
	 * @param rightsToo Нужно ли также увеличить счетчик верных ответов
	 * @return 0 или номер ошибки
	 */
	private int incTries(Riddle r, boolean rightsToo) throws DbConnectException, DaoException
	{
		int answer_count = r.getAnswerCount() + (rightsToo ? 1 : 0);
		int try_count = r.getTryCount() + 1;

		return dao.update(new Riddle(r.getId(), r.getUserId(), r.getTitle(), r.getText(), r.getImage(), r.getAnswers(), answer_count, try_count, r.getCreated(), null)) ? 0 : -1;
	}
}