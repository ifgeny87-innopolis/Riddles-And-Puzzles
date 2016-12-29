package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.dao.AnswerDao;
import ru.rap.models.Answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	Logger getLogger() { return log;}

	// Singleton
	private AnswerService() {}

	private static class LazySingleton
	{
		private static AnswerService instance = new AnswerService();
	}

	public static AnswerService getInstance()
	{
		return LazySingleton.instance;
	}

	// DAO
	private static AnswerDao dao = AnswerDao.getInstance();

	@Override
	AnswerDao getDao() { return dao;}

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
		return dao.insert(new Answer(user_id,riddle_id,answer,is_right));
	}

	public Map<UUID, Answer> getFor(UUID userId, UUID... riddleIds) throws DbConnectException, DaoException
	{
		String sql = "WHERE user_id='" + userId.toString() + "' AND (riddle_id=?" + new String(new char[riddleIds.length-1]).replace("\0", " OR riddle_id=?") + ")";

		Map<UUID, Answer> result = new HashMap<>();
		List<Answer> list = dao.select(sql, riddleIds);
		if(list == null)
			return result;

		list.forEach(a -> result.put(a.getRiddleId(), a));
		return result;
	}
}