package ru.rap.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.libraries.DaoLibrary;
import ru.rap.managers.DatabaseManager;
import ru.rap.models.Answer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Управление данными справочника отгадок
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
public class AnswerDao extends BaseDao<Answer>
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(AnswerDao.class);

	// Реализация Singleton
	private AnswerDao() {}

	private static AnswerDao instance = new AnswerDao();

	public static AnswerDao getInstance()
	{
		return instance;
	}

	// Таблица
	private static final String TABLE = "answer";

	// Шаблоны запросов
	private static final String SELECT = "SELECT id, user_id, riddle_id, answer, is_right, time_create FROM " + TABLE;
	private static final String INSERT = "INSERT INTO " + TABLE + " (user_id_bin, riddle_id_bin, answer, is_right) VALUES(unhex(?), unhex(?), ?, ?)";

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
		return null;
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
	public boolean insert(Answer pojo) throws DaoException, DbConnectException
	{
		try (PreparedStatement ps = DatabaseManager.prepareStatement(INSERT)) {
			ps.setString(1, DaoLibrary.getClearUuid(pojo.getUserId()));
			ps.setString(2, DaoLibrary.getClearUuid(pojo.getRiddleId()));
			ps.setString(3, pojo.getAnswer());
			ps.setInt(4, pojo.getIsRight() ? 1 : 0);
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
	public boolean update(Answer pojo) throws DaoException, DbConnectException
	{
		throw new DaoException("Запрещено обновлять отгадки");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  DELETE - методы удаления данных
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public boolean delete(Answer pojo) throws DaoException
	{
		throw new DaoException("Запрещено удалять отгадки");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  MAPPING - маппинг данных на новый объект
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public Answer mapRow(ResultSet rs) throws DaoException
	{
		try {
			return new Answer(
					// id
					UUID.fromString(rs.getString(1)),
					// user_id
					UUID.fromString(rs.getString(2)),
					// riddle_id
					UUID.fromString(rs.getString(3)),
					// answer
					rs.getString(4),
					// is_right
					rs.getInt(5) != 0,
					// created
					rs.getTimestamp(6)
			);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new DaoException("Mapping error: ", e);
		}
	}
}
