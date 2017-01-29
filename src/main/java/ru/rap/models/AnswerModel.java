package ru.rap.models;

import java.sql.Timestamp;

/**
 * Сущность отгадок
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
public class AnswerModel implements IModel
{
	//id
	private final Integer id;

	// id автора
	private final Integer answererId;

	// id загадки
	private final Integer riddleId;

	// текст отгадки
	private final String answer;

	// флаг правильного ответа
	private final boolean isRight;

	// время создания
	private final Timestamp created;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public AnswerModel(Integer id, Integer answererId, Integer riddleId, String answer, boolean isRight, Timestamp created)
	{
		this.id = id;
		this.answererId = answererId;
		this.riddleId = riddleId;
		this.answer = answer;
		this.isRight = isRight;
		this.created = created;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public Integer getId()
	{
		return id;
	}

	public Integer getAnswererId()
	{
		return answererId;
	}

	public Integer getRiddleId()
	{
		return riddleId;
	}

	public String getAnswer()
	{
		return answer;
	}

	public boolean getIsRight()
	{
		return isRight;
	}

	public Timestamp getCreated()
	{
		return created;
	}
}
