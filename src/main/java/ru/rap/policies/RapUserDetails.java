package ru.rap.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rap.entities.UserEntity;
import ru.rap.models.UserModel;
import ru.rap.repositories.UserRepository;
import ru.rap.services.UserService;

import java.util.Collection;
import java.util.List;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapUserDetails implements UserDetails
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RapUserDetails.class);

	private final UserEntity userEntity;
	private final UserModel userModel;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public RapUserDetails(UserEntity userEntity)
	{
		this.userEntity = userEntity;
		this.userModel = userService.toPojo(userEntity);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UserModel getUser()
	{
		return userModel;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Implements of UserDetails
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		List<GrantedAuthority> roles = userEntity.getGrantedAuthorities();
		return roles;
	}

	@Override
	public String getPassword()
	{
		String pwd = userEntity.getHashPassword();
		return pwd;
	}

	@Override
	public String getUsername()
	{
		return userModel.getName();
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