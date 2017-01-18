package ru.rap.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
	//  COUNT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public abstract int count(String condition, Object... args);

	public int count() {
		return count(null);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  SELECT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public abstract T selectOne(String condition, Object... args);

	public abstract T selectOneBy(String field, Object arg);

	public abstract List<T> select(String condition, Object... args);

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
