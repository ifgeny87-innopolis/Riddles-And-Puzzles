package ru.rap.policies;

import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rap.common.exceptions.DaoException;
import ru.rap.entities.UserEntity;
import ru.rap.models.UserModel;
import ru.rap.services.UserService;

import java.util.Collection;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapUserDetails implements UserDetails
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RapUserDetails.class);

	private final UserEntity user;

	private UserService userService;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  CONSTRUCTOR
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public RapUserDetails(@NotNull UserEntity user, @NotNull UserService userService)
	{
		this.user = user;
		this.userService = userService;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UserEntity getUser()
	{
		return user;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  IMPLEMENTS OF UserDetails
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		if (user == null) {
			throw new NullPointerException("Пользователь не может быть null");
		}
		Collection<? extends GrantedAuthority> roles = userService.getUserRoles(user);
		return roles;
	}

	@Override
	public String getPassword()
	{
		String pwd = user.getHashPassword();
		return pwd;
	}

	@Override
	public String getUsername()
	{
		return user.getName();
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}
}