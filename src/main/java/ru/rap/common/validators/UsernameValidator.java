package ru.rap.common.validators;

/**
 * Валидатор имени пользователя
 *
 * Package ru.rap.common.validators
 * Class UsernameValidator implements IValidator
 *
 * @author Makarov Evgeny, 2017
 */
public class UsernameValidator implements IValidator
{
	private static final String USERNAME_PATTERN =
			"([A-Za-z_\\d]{3,30})";

	@Override
	public boolean validate(Object o)
	{
		if (o != null && o instanceof String) {
			String username = (String) o;
			return username.matches(USERNAME_PATTERN);
		}
		return false;
	}
}
