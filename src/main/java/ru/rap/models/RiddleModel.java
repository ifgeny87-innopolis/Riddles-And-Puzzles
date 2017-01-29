package ru.rap.models;

import java.sql.Timestamp;

/**
 * Сущность загадки
 *
 * Created in project RiddlesAndPuzzles in 25.12.2016
 */
public class RiddleModel implements IModel
{
	//id
	private final Integer id;

	// id автора
	private final Integer riddlerId;

	// заголовок
	private final String title;

	// текст загадки
	private final String text;

	// ссылка на картинку
	private final String image;

	// правильный ответ (варианты ответов)
	private final String[] answers;

	// количество правильных ответов
	private final int answeredCount;

	// количество сделанных попыток
	private final int attemptCount;

	// время создания
	private final java.sql.Timestamp created;

	// время обновления
	private final java.sql.Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public RiddleModel(Integer id, Integer riddlerId, String title, String text, String image, String[] answers, int answeredCount, int attemptCount, Timestamp created, Timestamp updated)
	{
		this.id = id;
		this.riddlerId = riddlerId;
		this.title = title;
		this.text = text;
		this.image = image;
		this.answers = answers;
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

	public Integer getRiddlerId()
	{
		return riddlerId;
	}

	public String getTitle()
	{
		return title;
	}

	public String getText()
	{
		return text;
	}

	public String getImage()
	{
		return image;
	}

	public String[] getAnswers()
	{
		return answers;
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
