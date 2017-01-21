package ru.rap.models;

import java.sql.Date;

/**
 * Сущность пользователя
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public class UserModel extends BaseModel
{
	// имя пользователя
	private final String name;

	// количество верно отгаданных
	private final int answerCount;

	// количество попыток
	private final int tryCount;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public UserModel(Integer id, String name, int answerCount, int tryCount)
	{
		super(id);
		this.name = name;
		this.answerCount = answerCount;
		this.tryCount = tryCount;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public String getName()
	{
		return name;
	}

	public int getAnswerCount()
	{
		return answerCount;
	}

	public int getTryCount()
	{
		return tryCount;
	}
}
