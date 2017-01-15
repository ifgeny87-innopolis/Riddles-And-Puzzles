package ru.rap.policies;

import ru.rap.models.User;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapPrincipal implements Principal
{
	private User user;

	public User getUser()
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
