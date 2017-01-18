package ru.rap.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.entities.UserEntity;

import java.util.List;

/**
 * Управление данными справочника пользователей
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public class UserDao extends BaseDao<UserEntity>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(UserDao.class);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  PREPARE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	private Query<UserEntity> prepare(Session session, String condition, Object[] args)
	{
		String sql = (condition != null)
				? "from UserEntity"
				: condition;

		// create query
		Query<UserEntity> query = session
				.createQuery(sql, UserEntity.class);

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
	public UserEntity selectOne(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getSingleResult();
		}
	}

	@Override
	public UserEntity selectOneBy(String field, Object arg)
	{
		return selectOne(String.format("from UserEntity where %s=:value", field), arg);
	}

	@Override
	public List<UserEntity> select(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getResultList();
		}
	}

	@Override
	public List<UserEntity> selectBy(String field, Object arg)
	{
		return select(String.format("from UserEntity where %s=:value", field), arg);
	}
}
