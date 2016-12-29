package ru.rap.common.exceptions;

/**
 * Ошибка уровня выполнения запросов
 *
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
public class DaoException extends Exception
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  CONSTRUCTORS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public DaoException(String reason)
	{
		super(reason);
	}

	public DaoException(String reason, Throwable cause)
	{
		super(reason, cause);
	}
}
