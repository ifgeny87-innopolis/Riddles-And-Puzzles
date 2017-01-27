package ru.rap.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rap.entities.UserEntity;
import ru.rap.repositories.UserRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapUserDetails implements UserDetails
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RapUserDetails.class);

	private final UserEntity user;

	@Autowired
	private UserRepository userRepository;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public RapUserDetails(UserEntity user)
	{
		this.user = user;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UserEntity getUser()
	{
		return user;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Implements of UserDetails
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		List<GrantedAuthority> roles = user.getGrantedAuthorities();
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