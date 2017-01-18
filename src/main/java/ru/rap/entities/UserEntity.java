package ru.rap.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

/**
 * Сущность пользователя
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
@Entity
@Table(name = "user")
public class UserEntity
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private UUID uid;

	// имя пользователя
	@Column(nullable = false)
	private String name;

	// дата рождения
	@Column
	private Date birth;

	// хеш пароля
	@Column
	private String hash_password;

	// количество верно отгаданных
	@Column
	private int answer_count;

	// количество попыток
	@Column
	private int try_count;

	// созданные загадки
	@OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
	private RiddleEntity[] makedRiddles;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS & SETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UUID getUid()
	{
		return uid;
	}

	public UserEntity setUid(UUID uid)
	{
		this.uid = uid;
		return this;
	}

	public String getName()
	{
		return name;
	}

	public UserEntity setName(String name)
	{
		this.name = name;
		return this;
	}

	public Date getBirth()
	{
		return birth;
	}

	public UserEntity setBirth(Date birth)
	{
		this.birth = birth;
		return this;
	}

	public String getHashPassword()
	{
		return hash_password;
	}

	public UserEntity setHashPassword(String hash_password)
	{
		this.hash_password = hash_password;
		return this;
	}

	public int getAnswerCount()
	{
		return answer_count;
	}

	public UserEntity setAnswerCount(int answer_count)
	{
		this.answer_count = answer_count;
		return this;
	}

	public int getTryCount()
	{
		return try_count;
	}

	public UserEntity setTryCount(int try_count)
	{
		this.try_count = try_count;
		return this;
	}

	public RiddleEntity[] getMakedRiddles()
	{
		return makedRiddles;
	}

	public UserEntity setMakedRiddles(RiddleEntity[] makedRiddles)
	{
		this.makedRiddles = makedRiddles;
		return this;
	}
}
