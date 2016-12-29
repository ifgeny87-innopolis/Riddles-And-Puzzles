package ru.rap.models;

import java.sql.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность пользователя
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public class User extends BaseModel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	// имя пользователя
	private final String name;

	// дата рождения
	private final Date birth;

	// хеш пароля
	private final String hash_password;

	// количество верно отгаданных
	private final int answer_count;

	// количество попыток
	private final int try_count;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  CONSTRUCTORS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public User(UUID id, String name, Date birth, String hash_password, int answer_count, int try_count)
	{
		super(id);
		this.name = name;
		this.birth = birth;
		this.hash_password = hash_password;
		this.answer_count = answer_count;
		this.try_count = try_count;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public String getName()
	{
		return name;
	}

	public Date getBirth()
	{
		return birth;
	}

	public String getHashPassword()
	{
		return hash_password;
	}

	public int getAnswerCount()
	{
		return answer_count;
	}

	public int getTryCount()
	{
		return try_count;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  MAPPING
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public Map<String, Object> createPublicMap()
	{
//		Map<String, Object> map = new HashMap<>();
//		map.put("id", getId());
//		map.put("name", name);
//		if (birth != null)
//			map.put("birth", birth);
//		// hash_password не нужно добавлять в мапу
//		map.put("answer_count", name);
//		map.put("try_count", name);
		return null;
	}
}
