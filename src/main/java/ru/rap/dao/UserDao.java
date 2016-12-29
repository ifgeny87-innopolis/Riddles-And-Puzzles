package ru.rap.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.managers.DatabaseManager;
import ru.rap.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Управление данными справочника пользователей
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public class UserDao extends BaseDao<User>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(UserDao.class);

	// Реализация Singleton
	private UserDao() {}

	private static UserDao instance = new UserDao();

	public static UserDao getInstance()
	{
		return instance;
	}

	// Таблица
	private static final String TABLE = "user";

	// Шаблоны запросов
	private static final String SELECT = "SELECT id, name, birth, hash_password, answer_count, try_count FROM " + TABLE;
	private static final String INSERT = "INSERT INTO " + TABLE + " (name, birth, hash_password) VALUES(?, ?, ?)";
	private static final String UPDATE = "UPDATE " + TABLE + " SET name=?, birth=?, hash_password=?, answer_count=?, try_count=? WHERE id=?";

	@Override
	Logger getLogger()
	{
		return log;
	}

	@Override
	String getSource()
	{
		return TABLE;
	}

	@Override
	String getSelectTmp()
	{
		return SELECT;
	}

	@Override
	String getInsertTmp()
	{
		return INSERT;
	}

	@Override
	String getUpdateTmp()
	{
		return UPDATE;
	}

	@Override
	String getDeleteTmp()
	{
		return null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  INSERT - методы вставки данных
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public boolean insert(User pojo) throws DaoException, DbConnectException
	{
		try (PreparedStatement ps = DatabaseManager.prepareStatement(INSERT)) {
			ps.setString(1, pojo.getName());
			ps.setDate(2, pojo.getBirth());
			ps.setString(3, pojo.getHashPassword());
			log.trace("Insert ready: " + ps);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			String error = "Ошибка при операции INSERT:\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  UPDATE - методы обновления данных
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public boolean update(User pojo) throws DaoException, DbConnectException
	{
		try (PreparedStatement ps = DatabaseManager.prepareStatement(UPDATE)) {
			ps.setString(1, pojo.getName());
			ps.setDate(2, pojo.getBirth());
			ps.setString(3, pojo.getHashPassword());
			ps.setInt(4, pojo.getAnswerCount());
			ps.setInt(5, pojo.getTryCount());
			ps.setString(6, pojo.getId().toString());
			log.trace("Update ready: " + ps);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			String error = "Ошибка при операции UPDATE:\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  DELETE - методы удаления данных
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public boolean delete(User pojo) throws DaoException
	{
		return false;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  MAPPING - маппинг данных на новый объект
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public User mapRow(ResultSet rs) throws DaoException
	{
		try {
			return new User(
					// id
					UUID.fromString(rs.getString(1)),
					// name
					rs.getString(2),
					// birth
					rs.getDate(3),
					// hash_password
					rs.getString(4),
					// answer_count
					rs.getInt(5),
					// try_count
					rs.getInt(6)
			);
		} catch (SQLException e) {
			String error = "Сорвалось выполнение операции MAPPING:\n" + e.getMessage();
			getLogger().error(error, e);
			throw new DaoException(error, e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  EQUALS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
}
