package ru.rap.models;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность загадки
 *
 * Created in project RiddlesAndPuzzles in 25.12.2016
 */
public class Riddle extends BaseModel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	// id автора
	private final UUID user_id;

	// заголовок
	private final String title;

	// текст загадки
	private final String text;

	// ссылка на картинку
	private final String image;

	// правильный ответ (варианты ответов)
	private final String[] answers;

	// количество правильных ответов
	private final int answer_count;

	// количество сделанных попыток
	private final int try_count;

	// время создания
	private final java.sql.Timestamp created;

	// время обновления
	private final java.sql.Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  CONSTRUCTORS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Riddle(UUID id, UUID user_id, String title, String text, String image, String[] answers, int answer_count, int try_count, Timestamp created, Timestamp updated)
	{
		super(id);
		this.user_id = user_id;
		this.title = title;
		this.text = text;
		this.image = image;
		this.answers = answers;
		this.answer_count = answer_count;
		this.try_count = try_count;
		this.created = created;
		this.updated = updated;
	}

	public Riddle(UUID user_id, String title, String text, String image, String[] answers)
	{
		this(null, user_id, title, text, image, answers, 0, 0, null, null);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UUID getUserId()
	{
		return user_id;
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

	public int getAnswerCount()
	{
		return answer_count;
	}

	public int getTryCount()
	{
		return try_count;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	public Timestamp getUpdated()
	{
		return updated;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  MAPPING
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	protected Map<String, Object> createPublicMap()
	{
		return null;
	}
}
