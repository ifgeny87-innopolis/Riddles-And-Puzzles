package ru.rap.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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
	private Integer id;

	// имя пользователя
	@Column(nullable = false)
	private String name;

	// хеш пароля
	@Column(name = "hash_password")
	private String hashPassword;

	// количество верно отгаданных
	@Column(name = "answered_count")
	private int answeredCount;

	// количество попыток
	@Column(name = "attempt_count")
	private int attemptCount;

	// созданные загадки
	@OneToMany(mappedBy = "riddler", fetch = FetchType.LAZY)
	private List<RiddleEntity> makedRiddles;

	// время создания
	@Column(name = "time_create")
	private Timestamp created;

	// время обновления
	@Column(name = "time_update")
	private Timestamp updated;

	// роли
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role2user",
			joinColumns = @JoinColumn(name = "id_role"),
			inverseJoinColumns = @JoinColumn(name = "id_user"))
	private List<RoleEntity> roles;

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

	public String getHashPassword()
	{
		return hashPassword;
	}

	public int getAnsweredCount()
	{
		return answeredCount;
	}

	public int getAttemptCount()
	{
		return attemptCount;
	}

	public List<RiddleEntity> getMakedRiddles()
	{
		return makedRiddles;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	public Timestamp getUpdated()
	{
		return updated;
	}

	public List<RoleEntity> getRoles()
	{
		return roles;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Setters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UserEntity setName(String name)
	{
		this.name = name;
		return this;
	}

	public UserEntity setHashPassword(String hashPassword)
	{
		this.hashPassword = hashPassword;
		return this;
	}

	public UserEntity setAnsweredCount(int answeredCount)
	{
		this.answeredCount = answeredCount;
		return this;
	}

	public UserEntity setAttemptCount(int attemptCount)
	{
		this.attemptCount = attemptCount;
		return this;
	}

	public UserEntity setMakedRiddles(List<RiddleEntity> makedRiddles)
	{
		this.makedRiddles = makedRiddles;
		return this;
	}

	public UserEntity setRoles(List<RoleEntity> roles)
	{
		this.roles = roles;
		return this;
	}
}
