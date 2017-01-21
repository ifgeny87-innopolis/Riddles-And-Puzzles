package ru.rap.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Связь ролей и пользователей (many to many)
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
@Entity
@Table(name = "role2user")
public class Role2UserEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// роль
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_role",foreignKey = @ForeignKey(name = "fk_role_user"))
	private RoleEntity role;

	// автор
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_user_role"))
	private UserEntity user;

	// время создания
	@Column(name = "time_create")
	private Timestamp created;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public Integer getId()
	{
		return id;
	}

	public RoleEntity getRole()
	{
		return role;
	}

	public UserEntity getUser()
	{
		return user;
	}

	public Timestamp getCreated()
	{
		return created;
	}
}
