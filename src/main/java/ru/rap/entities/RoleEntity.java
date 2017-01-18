package ru.rap.entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Сущность роли пользователя
 *
 * Created in project RiddlesAndPuzzles in 17.01.2017
 */
@Entity
@Table(name = "role")
public class RoleEntity
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private UUID uid;

	@Column(nullable = false)
	private String name;

	@Column
	private String title;

	// время создания
	@Column
	private Timestamp created;

	// время обновления
	@Column
	private Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS & SETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UUID getUid()
	{
		return uid;
	}

	public RoleEntity setUid(UUID uid)
	{
		this.uid = uid;
		return this;
	}

	public String getName()
	{
		return name;
	}

	public RoleEntity setName(String name)
	{
		this.name = name;
		return this;
	}

	public String getTitle()
	{
		return title;
	}

	public RoleEntity setTitle(String title)
	{
		this.title = title;
		return this;
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
