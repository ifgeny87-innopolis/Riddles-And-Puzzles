package ru.rap.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.rap.entities.UserEntity;

import javax.persistence.TemporalType;
import java.util.List;

/**
 * Управление данными справочника пользователей
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
@Transactional
public class UserDao extends BaseDao<UserEntity>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(UserDao.class);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  PREPARE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	protected Query<UserEntity> prepare(Session session, String condition, Object[] args)
	{
		String hql = (condition == null)
				? "from UserEntity"
				: condition;

		// create query
		Query<UserEntity> query = session
				.createQuery(hql, UserEntity.class);

		// map args
		if (condition != null) {
			for (int i = 0; i < args.length; i++) {
				int k = i + 1;
				query.setParameter(k, args[i]);
			}
		}

		return query;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  SELECT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public UserEntity selectOneBy(String field, Object arg)
	{
		return selectOne(String.format("FROM UserEntity WHERE %s = ?1", field), arg);
	}

	@Override
	public List<UserEntity> selectBy(String field, Object arg)
	{
		return select(String.format("FROM UserEntity WHERE %s = ?1", field), arg);
	}
}
