package ru.rap.common.exceptions;

/**
 * Ошибка уровня подключения к СУБД
 *
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
public class DbConnectException extends Exception
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  CONSTRUCTORS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public DbConnectException(String reason)
	{
		super(reason);
	}

	public DbConnectException(String reason, Throwable cause)
	{
		super(reason, cause);
	}
}
