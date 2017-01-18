package ru.rap.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.entities.AnswerEntity;

import java.util.List;

/**
 * Управление данными справочника отгадок
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
public class AnswerDao extends BaseDao<AnswerEntity>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(AnswerDao.class);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  PREPARE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	private Query<AnswerEntity> prepare(Session session, String condition, Object[] args)
	{
		String sql = (condition != null)
				? "from AnswerEntity"
				: condition;

		// create query
		Query<AnswerEntity> query = session
				.createQuery(sql, AnswerEntity.class);

		// map args
		if (condition != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i + 1, args[i]);
			}
		}

		return query;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  SELECT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public int count(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getFetchSize();
		}
	}

	@Override
	public AnswerEntity selectOne(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getSingleResult();
		}
	}

	@Override
	public AnswerEntity selectOneBy(String field, Object arg)
	{
		return selectOne(String.format("from AnswerEntity where %s=:value", field), arg);
	}

	@Override
	public List<AnswerEntity> select(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getResultList();
		}
	}

	@Override
	public List<AnswerEntity> selectBy(String field, Object arg)
	{
		return select(String.format("from AnswerEntity where %s=:value", field), arg);
	}
}