package ru.rap.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.security.core.GrantedAuthority;
import ru.rap.roles.ROLE_ADMIN;
import ru.rap.roles.ROLE_USER;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Сущность роли пользователя
 *
 * Created in project RiddlesAndPuzzles in 17.01.2017
 */
@Entity
@Table(name = "role")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="role")
public class RoleEntity implements IEntity, Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column
	private String title;

	// время создания
	@Column(name = "time_create")
	private Timestamp created;

	// время обновления
	@Column(name = "time_update")
	private Timestamp updated;

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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Setters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public void setName(String name)
	{
		this.name = name;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Special
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public GrantedAuthority getGrantedAuthority() {
		if(name.equals("ROLE_ADMIN"))
			return new ROLE_ADMIN();
		else if(name.equals("ROLE_USER"))
			return new ROLE_USER();
		else
			return null;
	}
}
