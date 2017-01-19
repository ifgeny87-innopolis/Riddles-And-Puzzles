package ru.rap.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import ru.rap.entities.RoleEntity;
import ru.rap.entities.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Управление ролями
 *
 * Created in project RiddlesAndPuzzles in 18.01.2017
 */
public class RoleDao extends BaseDao<RoleEntity>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RoleDao.class);

	public Collection<? extends GrantedAuthority> getRolesByUser(UserEntity user) {
		List<GrantedAuthority> list = new ArrayList<>();
		List<RoleEntity> userRoles = user.getRoles();
		return user.getRoles()
				.stream()
				.map(RoleEntity::getGrantedAuthority)
				.collect(Collectors.toList());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  PREPARE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	private Query<RoleEntity> prepare(Session session, String condition, Object[] args)
	{
		String sql = (condition != null)
				? "from RoleEntity"
				: condition;

		// create query
		Query<RoleEntity> query = session
				.createQuery(sql, RoleEntity.class);

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
	public RoleEntity selectOne(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getSingleResult();
		}
	}

	@Override
	public RoleEntity selectOneBy(String field, Object arg)
	{
		return selectOne(String.format("from RoleEntity where %s=:value", field), arg);
	}

	@Override
	public List<RoleEntity> select(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getResultList();
		}
	}

	@Override
	public List<RoleEntity> selectBy(String field, Object arg)
	{
		return select(String.format("from RoleEntity where %s=:value", field), arg);
	}
}