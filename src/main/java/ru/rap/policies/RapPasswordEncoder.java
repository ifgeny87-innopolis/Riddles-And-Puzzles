package ru.rap.policies;

import ru.rap.libraries.HashLibrary;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder
{

	@Override
	public String encode(CharSequence rawPassword)
	{
		String result = HashLibrary.toMd5(rawPassword.toString());
		return result;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword)
	{
		boolean flag = HashLibrary.toMd5(rawPassword.toString()).equals(encodedPassword);
		return flag;
	}
}
