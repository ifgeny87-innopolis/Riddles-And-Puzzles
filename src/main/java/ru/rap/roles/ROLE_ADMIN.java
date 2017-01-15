package ru.rap.roles;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class ROLE_ADMIN implements GrantedAuthority
{
	@Override
	public String getAuthority()
	{
		return "ROLE_ADMIN";
	}
}
