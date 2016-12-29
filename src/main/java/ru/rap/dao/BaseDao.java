package ru.rap.dao;

import org.slf4j.Logger;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.common.exceptions.DaoException;
import ru.rap.libraries.DaoLibrary;
import ru.rap.managers.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	abstract Logger getLogger();

	abstract String getSource();

	// шаблон select
	abstract String getSelectTmp();

	// шаблон insert
	abstract String getInsertTmp();

	// шаблон update
	abstract String getUpdateTmp();

	// шаблон delete
	abstract String getDeleteTmp();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  TRANSACTION CONTROL
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Выполняет COMMIT
	 *
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public void commit() throws DbConnectException, DaoException
	{
		try {
			DatabaseManager.getConnection().commit();
		} catch (SQLException e) {
			String error = "Ошибка при операции COMMIT:\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	/**
	 * Выполняет ROLLBACK
	 *
	 * @throws DbConnectException
	 * @throws DaoException
	 */
	public void rollback() throws DbConnectException, DaoException
	{
		try {
			DatabaseManager.getConnection().rollback();
		} catch (SQLException e) {
			String error = "Ошибка при операции ROLLBACK:\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  COUNT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Вернет количество запсей по запросу
	 *
	 * @param condition Строка-шаблон расширения запроса (WHERE, ORDER, LIMIT)
	 * @param args      Аргументы строки-шаблона
	 * @return Количество строк
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public int count(String condition, Object... args) throws DaoException, DbConnectException
	{
		String sql = "SELECT COUNT(*) FROM " + getSource() + " " + condition;
		try (PreparedStatement ps = DatabaseManager.prepareStatement(sql)) {
			// маппинг полей
			DaoLibrary.mapStatement(ps, args);

			ResultSet rs = ps.executeQuery();
			// ошибки быть не должно, результат должен быть
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			String error = "Ошибка при операции SELECT COUNT(*):\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  SELECT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	/**
	 * Выбирает тоько одну запись по шаблону
	 *
	 * @param condition Строка-шаблон расширения запроса (WHERE, ORDER, LIMIT)
	 * @param args      Аргументы строки-шаблона
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public T selectOne(String condition, Object... args) throws DaoException, DbConnectException
	{
		String sql = getSelectTmp() + " " + condition;
		try (PreparedStatement ps = DatabaseManager.prepareStatement(sql)) {
			// маппинг полей
			DaoLibrary.mapStatement(ps, args);

			ResultSet rs = ps.executeQuery();
			return (rs.next())
					? mapRow(rs)
					: null;
		} catch (SQLException e) {
			String error = "Ошибка при операции SELECT:\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	/**
	 * Выбор одной записи по условию по полю
	 *
	 * @param field
	 * @param arg
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public T selectOneBy(String field, Object arg) throws DaoException, DbConnectException
	{
		return selectOne("WHERE " + field + "=?", arg);
	}

	/**
	 * Выборка списка с дополнительным условием
	 *
	 * @param condition Строка-шаблон расширения запроса (WHERE, ORDER, LIMIT)
	 * @param args      Аргументы строки-шаблона
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public List<T> select(String condition, Object... args) throws DaoException, DbConnectException
	{
		String sql = getSelectTmp() + " " + condition;
		try (PreparedStatement ps = DatabaseManager.prepareStatement(sql)) {
			// маппинг полей
			DaoLibrary.mapStatement(ps, args);

			ResultSet rs = ps.executeQuery();

			// чтобы узнать количество строк, прыгаю до конца, потом вперед
			if (!rs.last())
				return null;
			int rowCount = rs.getRow();
			rs.beforeFirst();

			// создаю и заполняю список
			List<T> list = new ArrayList<>();
			while (rs.next())
				list.add(mapRow(rs));

			return list;
		} catch (SQLException e) {
			String error = "Ошибка при операции SELECT:\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	/**
	 * Выборка списка по одному полю
	 *
	 * @param field
	 * @param arg
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public List<T> selectBy(String field, Object arg) throws DaoException, DbConnectException
	{
		return select("WHERE " + field + "=?", arg);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  INSERT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public abstract boolean insert(T pojo) throws DaoException, DbConnectException;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  UPDATE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public abstract boolean update(T pojo) throws DaoException, DbConnectException;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  DELETE
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public abstract boolean delete(T pojo) throws DaoException, DbConnectException;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  MAPPING
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public abstract T mapRow(ResultSet rs) throws DaoException;
}
