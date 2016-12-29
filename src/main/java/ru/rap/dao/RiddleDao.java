package ru.rap.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.common.exceptions.DaoException;
import ru.rap.libraries.DaoLibrary;
import ru.rap.managers.DatabaseManager;
import ru.rap.models.Riddle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Управление данными справочника загадок и ребусов
 *
 * Created in project RiddlesAndPuzzles in 25.12.2016
 */
public class RiddleDao extends BaseDao<Riddle>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RiddleDao.class);

	// Singleton
	private RiddleDao() {}

	private static class LazySingleton
	{
		private static RiddleDao instance = new RiddleDao();
	}

	public static RiddleDao getInstance()
	{
		return LazySingleton.instance;
	}

	// Таблица
	private static final String TABLE = "riddle";

	// Шаблоны запросов
	private static final String SELECT = "SELECT id, user_id, title, text, image, answer, answer_count, try_count, time_create, time_update FROM " + TABLE;
	private static final String INSERT = "INSERT INTO " + TABLE + " (user_id_bin, title, text, image, answer) VALUES(unhex(?), ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE " + TABLE + " SET title=?, text=?, image=?, answer=?, answer_count=?, try_count=? WHERE id=?";
	private static final String DELETE = "DELETE FROM " + TABLE + " WHERE id=?";

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
		return DELETE;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  INSERT - методы вставки данных
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public boolean insert(Riddle pojo) throws DaoException, DbConnectException
	{
		try (PreparedStatement ps = DatabaseManager.prepareStatement(INSERT)) {
			// по шаблону мапаю поля
			ps.setString(1, DaoLibrary.getClearUuid(pojo.getUserId()));
			ps.setString(2, pojo.getTitle());
			ps.setString(3, pojo.getText());
			ps.setString(4, pojo.getImage());
			ps.setString(5, String.join(",", pojo.getAnswers()));
			log.trace("Insert ready: " + ps);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new DaoException("Insert error: ", e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  UPDATE - методы обновления данных
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public boolean update(Riddle pojo) throws DaoException, DbConnectException
	{
		try (PreparedStatement ps = DatabaseManager.prepareStatement(UPDATE)) {
			// по шаблону мапаю поля
			ps.setString(1, pojo.getTitle());
			ps.setString(2, pojo.getText());
			ps.setString(3, pojo.getImage());
			ps.setString(4, String.join(",", pojo.getAnswers()));
			ps.setInt(5, pojo.getAnswerCount());
			ps.setInt(6, pojo.getTryCount());
			ps.setString(7, pojo.getId().toString());
			log.info("Update ready: " + ps);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new DaoException("Update error: ", e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  DELETE - методы удаления данных
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public boolean delete(Riddle pojo) throws DaoException, DbConnectException
	{
		try (PreparedStatement ps = DatabaseManager.prepareStatement(DELETE)) {
			// по шаблону мапаю поля
			ps.setString(1, pojo.getId().toString());
			log.info("Delete ready: " + ps);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new DaoException("Delete error: ", e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  MAPPING - маппинг данных на новый объект
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public Riddle mapRow(ResultSet rs) throws DaoException
	{
		try {
			return new Riddle(
					// id
					UUID.fromString(rs.getString(1)),
					// user_id
					UUID.fromString(rs.getString(2)),
					// title
					rs.getString(3),
					// text
					rs.getString(4),
					// image
					rs.getString(5),
					// answer
					rs.getString(6).split(","),
					// answer_count
					rs.getInt(7),
					// try_count
					rs.getInt(8),
					// created
					rs.getTimestamp(9),
					// updated
					rs.getTimestamp(10)
			);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new DaoException("Mapping error: ", e);
		}
	}
}
