package ru.rap.models;

import java.sql.Timestamp;

/**
 * Сущность загадки
 *
 * Created in project RiddlesAndPuzzles in 25.12.2016
 */
public class RoleModel implements IModel
{
	//id
	private final Integer id;

	// название роли
	private final String name;

	// описание
	private final String title;

	// время создания
	private final Timestamp created;

	// время обновления
	private final Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public RoleModel(Integer id, String name, String title, Timestamp created, Timestamp updated)
	{
		this.id = id;
		this.name = name;
		this.title = title;
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

	public String getTitle()
	{
		return title;
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
