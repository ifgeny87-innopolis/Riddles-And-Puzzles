package ru.rap.models;

import java.sql.Timestamp;

/**
 * Сущность пользователя
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public class UserModel implements IModel
{
	//id
	private final Integer id;

	// имя пользователя
	private final String name;

	// количество верно отгаданных
	private final int answeredCount;

	// количество попыток
	private final int attemptCount;

	// время создания
	private final java.sql.Timestamp created;

	// время обновления
	private final java.sql.Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public UserModel(Integer id, String name, int answeredCount, int attemptCount, Timestamp created, Timestamp updated)
	{
		this.id = id;
		this.name = name;
		this.answeredCount = answeredCount;
		this.attemptCount = attemptCount;
		this.created = created;
		this.updated = updated;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public Integer getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getAnsweredCount()
	{
		return answeredCount;
	}

	public int getAttemptCount()
	{
		return attemptCount;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	public Timestamp getUpdated()
	{
		return updated;
	}
}
