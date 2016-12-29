package ru.rap.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rap.common.exceptions.DbConnectException;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

/**
 * Менеджер базы данных
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public class DatabaseManager
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

	private DatabaseManager() { }

	/** lazy singleton **/
	private static class LazyConnection
	{
		private static InitialContext ic;
		private static DataSource ds;
		private static Connection connection;

		static {
			try {
				ic = new InitialContext();
				ds = (DataSource) ic.lookup("java:comp/env/jdbc/db");
				connection = ds.getConnection();
			} catch (Exception e) {
				log.error("Не смогла программа прочиать настройку DataSource:\n" + e.getMessage(), e);
			}
		}
	}

	/**
	 * Подключается в БД
	 *
	 * @return Объект Connection
	 * @throws SQLException Ошибка соединения с БД
	 */
	public static Connection getConnection() throws DbConnectException
	{
		if (LazyConnection.connection == null) {
			throw new DbConnectException("Не удается подключиться в СУБД");
		}
		return LazyConnection.connection;
	}

	/**
	 * Создает Statement
	 *
	 * @return
	 * @throws SQLException
	 */
	public static Statement createStatement() throws DbConnectException, SQLException
	{
		return getConnection().createStatement();
	}

	/**
	 * Создает PreparedStatement
	 *
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static PreparedStatement prepareStatement(String sql) throws SQLException, DbConnectException
	{
		return getConnection().prepareStatement(sql);
	}
}
