package ru.rap.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rap.common.exceptions.DaoException;
import ru.rap.models.User;
import ru.rap.services.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapUserDetails implements UserDetails
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RapUserDetails.class);

	private final User user;

	private UserService userService;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  CONSTRUCTOR
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public RapUserDetails(User user, UserService userService) {
		this.user = user;
		this.userService = userService;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public User getUser() {
		return user;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  IMPLEMENTS OF UserDetails
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		try {
			if (user == null) {
				throw new NullPointerException("Пользователь не может быть null");
			}
			return userService.getUserRoles(user);
		} catch (DaoException e) {
			log.error("Не удалось получить список ролей для пользователя `" + user.getName() + "' по причине: " + e.getMessage(), e);
			throw new RuntimeException("Ошибка получения данных", e);
		}
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