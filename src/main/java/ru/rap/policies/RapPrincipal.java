package ru.rap.policies;

import ru.rap.models.UserModel;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapPrincipal implements Principal
{
	private UserModel user;

	public UserModel getUser()
	{
		return user;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean implies(Subject subject)
	{
		return false;
	}
}
