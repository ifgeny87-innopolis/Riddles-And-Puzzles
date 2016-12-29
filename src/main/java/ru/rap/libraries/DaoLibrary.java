package ru.rap.libraries;

import java.sql.*;
import java.util.UUID;

/**
 * Класс-помощник для работы с DAO
 *
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
public class DaoLibrary
{
	private DaoLibrary() { }

	/**
	 * Для списка переменных сетятся поля в PrepareStatement
	 *
	 * @param ps   {@link PreparedStatement}
	 * @param args Список значений для сета
	 * @throws SQLException Возможна ошибка
	 */
	public static void mapStatement(PreparedStatement ps, Object[] args) throws SQLException
	{
		for (int i = 1; i <= args.length; ++i) {
			Object o = args[i - 1];

			if (o == null) ps.setObject(i, null);
			else if (o instanceof String) ps.setString(i, (String) o);
			else if (o instanceof Integer) ps.setInt(i, (Integer) o);
			else if (o instanceof Float) ps.setFloat(i, (Float) o);
			else if (o instanceof Double) ps.setDouble(i, (Double) o);
			else if (o instanceof Boolean) ps.setBoolean(i, (Boolean) o);
			else if (o instanceof UUID) ps.setString(i, o.toString());
			else if (o instanceof Date) ps.setDate(i, (Date) o);
			else if (o instanceof Time) ps.setTime(i, (Time) o);
			else if (o instanceof Timestamp) ps.setTimestamp(i, (Timestamp) o);
			else
				// FIXME: 24.12.2016 может еще какие типы забыл?
				ps.setObject(i, o);
		}
	}

	/**
	 * Приводит UUID к строке и убирает дефисы.
	 * Используется, например, для созранения UUID в СУБД MySQL.
	 *
	 * @param id
	 * @return
	 */
	public static String getClearUuid(UUID id)
	{
		return id.toString().replaceAll("[-]", "");
	}
}
