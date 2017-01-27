package ru.rap.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.rap.entities.UserEntity;
import ru.rap.repositories.UserRepository;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapUserDetailsService implements UserDetailsService
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RapUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		UserEntity user = userRepository.findByName(username);
		if (user == null) {
			// пользователь нихт
			throw new UsernameNotFoundException(
					String.format("Пользователь по имени '%s' не найден", username));
		}
		return new RapUserDetails(user);
	}
}
