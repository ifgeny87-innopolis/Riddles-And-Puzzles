package ru.rap.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.common.exceptions.DaoException;
import ru.rap.entities.UserEntity;
import ru.rap.models.BaseModel;

import java.sql.ResultSet;
import java.util.List;

/**
 * Базовый класс работы с данными.
 * Классы-потомки реализуют модель SIUD (она же CRUD)
 *
 * Интерфейсы использовать не получится, в базовом классе реализована логика.
 *
 * Наследники должны реализовать синглтоны, потому что абстрактной статики не бывает.
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public abstract class BaseDao<T>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(BaseDao.class);

	@Autowired
	protected SessionFactory sessionFactory;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  PREPARE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	protected abstract Query<T> prepare(Session session, String condition, Object[] args);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  COUNT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public int count(String condition, Object... args) {
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getFetchSize();
		}
	}

	public int count() {
		return count(null);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  SELECT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public T selectOne(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.setMaxResults(1)
					.getSingleResult();
		}
	}

	public abstract T selectOneBy(String field, Object arg);

	public List<T> select(String condition, Object... args)
	{
		try (Session session = sessionFactory.openSession()) {
			return prepare(session, condition, args)
					.getResultList();
		}
	}

	public abstract List<T> selectBy(String field, Object arg);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  INSERT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public void insert(T entity)
	{
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			session.save(entity);

			tx.commit();
			tx = null;
		} finally {
			if (tx != null)
				tx.rollback();
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  UPDATE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public void update(T entity)
	{
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			session.update(entity);

			tx.commit();
			tx = null;
		} finally {
			if (tx != null)
				tx.rollback();
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  DELETE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public void delete(T entity)
	{
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			session.delete(entity);

			tx.commit();
			tx = null;
		} finally {
			if (tx != null)
				tx.rollback();
		}
	}
}
