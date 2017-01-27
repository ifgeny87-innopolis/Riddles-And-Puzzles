package ru.rap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.entities.AnswerEntity;
import ru.rap.entities.RiddleEntity;
import ru.rap.entities.UserEntity;
import ru.rap.models.AnswerModel;
import ru.rap.repositories.AnswerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private AnswerRepository answerRepository;

	/**
	 * Решил ли пользователь загадку
	 */
	public boolean isAnswerRight(int id_user, int id_riddle) throws DbConnectException, DaoException
	{
		int result = answerRepository.isAnswerRight(id_user, id_riddle);
		return result > 0;
	}

	/**
	 * Добавляет новую запись в справочник
	 *
	 * @param answer
	 * @param isRight
	 * @return
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public void insert(UserEntity user, RiddleEntity riddle, String answer, boolean isRight)
	{
		answerRepository.save(
				new AnswerEntity()
						.setAnswerer(user)
						.setRiddle(riddle)
						.setAnswer(answer)
						.setIsRight(isRight)
		);
	}

	public Map<Integer, AnswerEntity> getFor(int userId, int...riddleIds)
	{
		List<AnswerEntity> list = answerRepository.getFor(userId, riddleIds);

		if (list == null)
			return null;

		Map<Integer, AnswerEntity> result = new HashMap<>();
		list.forEach(a -> result.put(a.getRiddle().getId(), a));
		return result;
	}
}