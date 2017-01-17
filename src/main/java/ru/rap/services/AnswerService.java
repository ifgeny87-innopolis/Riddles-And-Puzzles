package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.dao.AnswerDao;
import ru.rap.libraries.StringLibrary;
import ru.rap.models.Answer;

import java.util.*;
import java.util.stream.Stream;

/**
 * Сервис работы с отгадками
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
public class AnswerService extends BaseService<Answer>
{
	// Logger
	private static final Logger log = LoggerFactory.getLogger(AnswerService.class);

	@Override
	protected Logger getLogger() { return log;}

	@Autowired
	private AnswerDao dao;

	@Override
	protected AnswerDao getDao() { return dao;}

	/**
	 * Решил ли пользователь загадку
	 *
	 * @param user_id   Номер пользователя
	 * @param riddle_id Номер загадки
	 * @return Решил или нет
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public boolean isAnswerRight(UUID user_id, UUID riddle_id) throws DbConnectException, DaoException
	{
		int result = dao.count("WHERE user_id=? AND riddle_id=? AND is_right=1", user_id, riddle_id);
		return result > 0;
	}

	/**
	 * Добавляет новую запись в справочник
	 *
	 * @param user_id
	 * @param riddle_id
	 * @param answer
	 * @param is_right
	 * @return
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public boolean insert(UUID user_id, UUID riddle_id, String answer, boolean is_right) throws DbConnectException, DaoException
	{
		return dao.insert(new Answer(user_id, riddle_id, answer, is_right));
	}

	public Map<UUID, Answer> getFor(UUID userId, UUID... riddleIds) throws DbConnectException, DaoException
	{
		String sql = "WHERE user_id=? AND is_right=1 AND riddle_id IN ("
				+ StringLibrary.repeat("?", ",", riddleIds.length) + ")";

		UUID[] args = new UUID[riddleIds.length + 1];
		args[0] = userId;
		for (int i = 0; i < riddleIds.length; i++) {
			args[i + 1] = riddleIds[i];
		}

		List<Answer> list = dao.select(sql, args);

		if (list == null)
			return null;

		Map<UUID, Answer> result = new HashMap<>();
		list.forEach(a -> result.put(a.getRiddleId(), a));
		return result;
	}
}