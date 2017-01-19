package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.dao.AnswerDao;
import ru.rap.entities.AnswerEntity;
import ru.rap.entities.RiddleEntity;
import ru.rap.entities.UserEntity;
import ru.rap.libraries.StringLibrary;
import ru.rap.models.AnswerModel;

import java.util.*;
import java.util.stream.Stream;

/**
 * Сервис работы с отгадками
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
public class AnswerService extends BaseService<AnswerModel>
{
	// Logger
	private static final Logger log = LoggerFactory.getLogger(AnswerService.class);

	@Autowired
	private AnswerDao dao;

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
	 * @param answer
	 * @param is_right
	 * @return
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public void insert(UserEntity user, RiddleEntity riddle, String answer, boolean is_right)
	{
		dao.insert(new AnswerEntity()
				.setAnswerer(user)
				.setRiddle(riddle)
				.setAnswer(answer)
				.setRight(is_right));
	}

	public Map<UUID, AnswerEntity> getFor(UUID userId, UUID... riddleIds) throws DbConnectException, DaoException
	{
		String sql = "WHERE user_id=? AND is_right=1 AND riddle_id IN ("
				+ StringLibrary.repeat("?", ",", riddleIds.length) + ")";

		UUID[] args = new UUID[riddleIds.length + 1];
		args[0] = userId;
		for (int i = 0; i < riddleIds.length; i++) {
			args[i + 1] = riddleIds[i];
		}

		List<AnswerEntity> list = dao.select(sql, args);

		if (list == null)
			return null;

		Map<UUID, AnswerEntity> result = new HashMap<>();
		list.forEach(a -> result.put(a.getRiddle().getUid(), a));
		return result;
	}
}