package ru.rap.models;

import java.sql.Timestamp;

/**
 * Сущность загадки
 *
 * Created in project RiddlesAndPuzzles in 25.12.2016
 */
public class RiddleModel extends BaseModel
{
	// id автора
	private final Integer userId;

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
	private final int tryCount;

	// время создания
	private final java.sql.Timestamp created;

	// время обновления
	private final java.sql.Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public RiddleModel(Integer id, Integer userId, String title, String text, String image, String[] answers, int answeredCount, int tryCount, Timestamp created, Timestamp updated)
	{
		super(id);
		this.userId = userId;
		this.title = title;
		this.text = text;
		this.image = image;
		this.answers = answers;
		this.answeredCount = answeredCount;
		this.tryCount = tryCount;
		this.created = created;
		this.updated = updated;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public Integer getUserId()
	{
		return userId;
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

	public String[] getAnswer()
	{
		return answers;
	}

	public int getAnsweredCount()
	{
		return answeredCount;
	}

	public int getTryCount()
	{
		return tryCount;
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
