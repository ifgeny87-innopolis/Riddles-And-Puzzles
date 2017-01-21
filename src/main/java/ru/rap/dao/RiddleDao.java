package ru.rap.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.rap.entities.RiddleEntity;

import java.util.List;

/**
 * Управление данными справочника загадок и ребусов
 *
 * Created in project RiddlesAndPuzzles in 25.12.2016
 */
@Transactional
public class RiddleDao extends BaseDao<RiddleEntity>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RiddleDao.class);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  PREPARE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	protected Query<RiddleEntity> prepare(Session session, String condition, Object[] args)
	{
		String sql = (condition == null)
				? "from RiddleEntity"
				: condition;

		// create query
		Query<RiddleEntity> query = session
				.createQuery(sql, RiddleEntity.class);

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
	public RiddleEntity selectOneBy(String field, Object arg)
	{
		return selectOne(String.format("from RiddleEntity where %s=:value", field), arg);
	}

	@Override
	public List<RiddleEntity> selectBy(String field, Object arg)
	{
		return select(String.format("from RiddleEntity where %s=:value", field), arg);
	}
}