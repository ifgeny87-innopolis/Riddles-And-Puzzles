package ru.rap.models;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность отгадок
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
public class Answer extends BaseModel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	// id автора
	private final UUID user_id;

	// id загадки
	private final UUID riddle_id;

	// текст отгадки
	private final String answer;

	// флаг правильного ответа
	private final boolean is_right;

	// время создания
	private final Timestamp created;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  CONSTRUCTORS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Answer(UUID id, UUID user_id, UUID riddle_id, String answer, boolean is_right, Timestamp created)
	{
		super(id);
		this.user_id = user_id;
		this.riddle_id = riddle_id;
		this.answer = answer;
		this.is_right = is_right;
		this.created = created;
	}

	public Answer(UUID user_id, UUID riddle_id, String answer, boolean is_right)
	{
		this(null, user_id, riddle_id, answer, is_right, null);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UUID getUserId()
	{
		return user_id;
	}

	public UUID getRiddleId()
	{
		return riddle_id;
	}

	public String getAnswer()
	{
		return answer;
	}

	public boolean getIsRight()
	{
		return is_right;
	}

	public Timestamp getCreated()
	{
		return created;
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
